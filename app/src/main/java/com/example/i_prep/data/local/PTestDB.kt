package com.example.i_prep.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.i_prep.data.local.model.PTest
import com.example.i_prep.data.local.model.THistory

@TypeConverters(TypeConverterDB::class)
@Database(entities = [PTest::class, THistory::class], version = 2)
abstract class PTestDB : RoomDatabase() {
    abstract val pTestDao: PTestDao
}