package com.boostcampai.foodlog.view

import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.boostcampai.foodlog.R
import com.boostcampai.foodlog.adapter.ResultItem
import com.boostcampai.foodlog.adapter.ResultMultiViewRecyclerAdapter
import com.boostcampai.foodlog.convertBitmapToBase64
import com.boostcampai.foodlog.databinding.FragmentResultBinding
import com.boostcampai.foodlog.drawBoundingBoxes
import com.boostcampai.foodlog.viewmodel.ResultViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException

@AndroidEntryPoint
class ResultFragment : Fragment() {

    private lateinit var binding: FragmentResultBinding
    private val viewModel: ResultViewModel by viewModels()
    private val navArgs: ResultFragmentArgs by navArgs()
    private lateinit var adapter: ResultMultiViewRecyclerAdapter

    private val storageRequestCode = 200

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_result, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        viewModel.loadInferenceResult(navArgs.inferenceResult)

        adapter = ResultMultiViewRecyclerAdapter { pos -> viewModel.removeFood(pos) }
        setToolbar()
        setRecyclerAdapter()
        initObservers()
    }

    private fun setToolbar() {
        binding.toolbar.apply {
            inflateMenu(R.menu.menu_toolbar_result)
            setOnMenuItemClickListener {
                if (it.itemId == R.id.action_save) {
                    saveImage(navArgs.bitmap).let { uri ->
                        viewModel.saveResult(uri.toString())

                        val action = ResultFragmentDirections.actionResultFragmentToHomeFragment()
                        findNavController().navigate(action)
                        return@setOnMenuItemClickListener true
                    }
                } else if (it.itemId == R.id.action_feedback) {
                    viewModel.inferenceResult.value?.foods?.let { foods ->
                        FeedbackDialogFragment(navArgs.bitmap, foods) { feedback ->
                            viewModel.sendFeedback(
                                convertBitmapToBase64(navArgs.bitmap),
                                feedback
                            ) { resultMessage ->
                                Toast.makeText(requireContext(), resultMessage, Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }.show(requireActivity().supportFragmentManager, "피드백 보내기")
                    }
                }
                return@setOnMenuItemClickListener false
            }
        }
    }

    private fun setRecyclerAdapter() {
        binding.rvResultRoot.adapter = adapter
        adapter.unit = viewModel.unit.value ?: "kcal"
        adapter.goal = viewModel.goal.value ?: 2000

        val header = viewModel.inferenceResult.value?.let { ResultItem.Header(it) }
        val foods = viewModel.inferenceResult.value?.foods ?: listOf()

        adapter.submitList(
            listOf(
                header,
                ResultItem.Image(navArgs.bitmap.drawBoundingBoxes(foods.map { it.pos })),
                ResultItem.Foods(foods, navArgs.bitmap)
            )
        )
    }

    private fun initObservers() {
        viewModel.goal.observe(viewLifecycleOwner, { setRecyclerAdapter() })
        viewModel.unit.observe(viewLifecycleOwner, { setRecyclerAdapter() })
        viewModel.inferenceResult.observe(viewLifecycleOwner, { setRecyclerAdapter() })
    }

    private fun saveImage(bitmap: Bitmap): Uri? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            saveImageAboveQ(bitmap)
        } else {
            if (storagePermissionsGranted)
                saveImageUnderQ(bitmap)
            else {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(
                        android.Manifest.permission.READ_EXTERNAL_STORAGE,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ),
                    100
                )
                if (storagePermissionsGranted)
                    saveImage(bitmap)
                else
                    null
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun saveImageAboveQ(bitmap: Bitmap): Uri? {
        val contentResolver = requireActivity().contentResolver
        val fileName = "${viewModel.inferenceResult.value!!.run { "$date $time" }}.png"

        val contentValues = ContentValues()
        contentValues.apply {
            put(MediaStore.Images.Media.RELATIVE_PATH, "DCIM/FoodLog")
            put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
            put(MediaStore.Images.Media.MIME_TYPE, "image/png")
            put(MediaStore.Images.Media.IS_PENDING, 1) // 경로 독점 설정
        }

        // Uri 설정
        val uri =
            contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

        try {
            if (uri != null) {
                val image = contentResolver.openFileDescriptor(uri, "w", null)

                if (image != null) {
                    val fos = FileOutputStream(image.fileDescriptor)
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)
                    fos.close()

                    contentValues.clear()
                    contentValues.put(MediaStore.Images.Media.IS_PENDING, 0) // 경로 독점 설정 해제
                    contentResolver.update(uri, contentValues, null, null)

                    return uri
                }
            }
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null
    }

    private fun saveImageUnderQ(bitmap: Bitmap): Uri? {
        val fileName = "${viewModel.inferenceResult.value!!.run { "$date $time" }}.png"
        val externalStorage = Environment.getExternalStorageDirectory().absolutePath
        val path = "$externalStorage/DCIM/FoodLog"
        val dir = File(path)

        if (!dir.exists())
            dir.mkdirs()

        try {
            val fileItem = File("$dir/$fileName")
            fileItem.createNewFile()

            val fos = FileOutputStream(fileItem)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)
            fos.close()

            val uri = Uri.fromFile(fileItem)
            requireActivity().sendBroadcast(
                Intent(
                    Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                    uri
                )
            )

            return uri
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null
    }

    private val storagePermissionsGranted
        get() =
            ContextCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
}
