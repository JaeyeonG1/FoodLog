package com.boostcampai.foodlog

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.boostcampai.foodlog.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment)
        navController = navHostFragment!!.findNavController()
        binding.bottomNavigation.setupWithNavController(navController)

        val topLevelDestSet = hashSetOf(
            R.id.homeFragment, R.id.cameraFragment, R.id.detailFragment
        )

        val appBarConfig = AppBarConfiguration.Builder(topLevelDestSet).build()
        binding.toolbar.setupWithNavController(navController, appBarConfig)

        navController.addOnDestinationChangedListener { controller, dest, args ->
            binding.toolbar.visibility = View.VISIBLE
            binding.toolbar.titleMarginStart = 10
            setSupportActionBar(binding.toolbar)
            supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_new_24)
            val customToolbarSet = setOf(R.id.resultFragment)

            if (customToolbarSet.contains(dest.id) || topLevelDestSet.contains(dest.id))
                binding.toolbar.visibility = View.GONE
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
