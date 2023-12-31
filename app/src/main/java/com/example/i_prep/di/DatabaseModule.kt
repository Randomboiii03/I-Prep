package com.example.i_prep.di

import android.content.Context
import androidx.room.Room
import com.example.i_prep.data.local.PTestDB
import com.example.i_prep.data.local.PTestDao
import com.example.i_prep.data.repository.DataStoreRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun providePTestDB(@ApplicationContext context: Context): PTestDB {
        return Room.databaseBuilder(
            context,
            PTestDB::class.java,
            "i-prep"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    @Singleton
    fun provideDataStoreRepository(@ApplicationContext context: Context) =
        DataStoreRepository(context = context)

    @Provides
    @Singleton
    fun providePTestDao(pTestDB: PTestDB): PTestDao = pTestDB.pTestDao
}