package com.example.i_prep.domain.repository

import com.example.i_prep.data.local.model.PTest
import com.example.i_prep.data.local.model.THistory
import kotlinx.coroutines.flow.Flow

interface Repository {
    fun getAllTest(): Flow<List<PTest>>
    fun getTestById(testId: Int): Flow<PTest>
    suspend fun upsertTest(pTest: PTest)
    suspend fun deleteTest(pTest: PTest)

    fun getAllHistory(): Flow<List<THistory>>
    fun getHistoryById(historyId: Int): Flow<THistory>
    fun getLastHistory(): Flow<THistory>
    suspend fun insertHistory(tHistory: THistory)
    suspend fun deleteHistory(tHistory: THistory)
}