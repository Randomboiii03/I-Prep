package com.example.i_prep.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.i_prep.data.local.model.PTest
import com.example.i_prep.data.local.model.THistory
import kotlinx.coroutines.flow.Flow

@Dao
interface PTestDao {
    //PTest table queries

    @Query("SELECT * FROM pTest")
    fun getAllTest(): Flow<List<PTest>>

    @Query("SELECT * FROM pTest where testId = :testId")
    fun getTestById(testId: Int): Flow<PTest>

    @Upsert
    suspend fun upsertTest(pTest: PTest)

    @Delete
    suspend fun deleteTest(pTest: PTest)

    //History table queries

    @Query("SELECT * FROM history")
    fun getAllHistory(): Flow<List<THistory>>

    @Query("SELECT * FROM history WHERE historyId = :historyId")
    fun getHistoryById(historyId: Int): Flow<THistory>

    @Query("SELECT * FROM history ORDER BY historyId DESC LIMIT 1")
    fun getLastHistory(): Flow<THistory>

    @Upsert
    suspend fun upsertHistory(tHistory: THistory)

    @Delete
    suspend fun deleteHistory(tHistory: THistory)
}