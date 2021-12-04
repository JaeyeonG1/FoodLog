package com.boostcampai.foodlog.di

import android.content.Context
import androidx.room.Room
import com.boostcampai.foodlog.data.AppDatabase
import com.boostcampai.foodlog.data.dao.DietDao
import com.boostcampai.foodlog.data.dao.FoodDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "Foodlog"
        ).build()
    }

    @Provides
    fun provideDietDao(appDatabase: AppDatabase): DietDao{
        return appDatabase.dietDao()
    }

    @Provides
    fun provideFoodDao(appDatabase: AppDatabase): FoodDao{
        return appDatabase.foodDao()
    }
}
