package com.example.i_prep.data.repository

import com.example.i_prep.data.local.PTestDao
import com.example.i_prep.data.local.model.PTest
import com.example.i_prep.data.local.model.THistory
import com.example.i_prep.domain.repository.Repository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PTestRepositoryImpl @Inject constructor(private val pTestDao: PTestDao): Repository {
    override fun getAllTest(): Flow<List<PTest>> {
        return pTestDao.getAllTest()
    }

    override fun getTestById(testId: Int): Flow<PTest> {
        return pTestDao.getTestById(testId)
    }

    override suspend fun upsertTest(pTest: PTest) {
        return pTestDao.upsertTest(pTest)
    }

    override suspend fun deleteTest(pTest: PTest) {
        return pTestDao.deleteTest(pTest)
    }

    override fun getAllHistory(): Flow<List<THistory>> {
        return pTestDao.getAllHistory()
    }

    override fun getHistoryById(historyId: Int): Flow<THistory> {
        return pTestDao.getHistoryById(historyId)
    }

    override fun getLastHistory(): Flow<THistory> {
        return pTestDao.getLastHistory()
    }

    override suspend fun upsertHistory(tHistory: THistory) {
        return pTestDao.upsertHistory(tHistory)
    }

    override suspend fun deleteHistory(tHistory: THistory) {
        return pTestDao.deleteHistory(tHistory)
    }
}