package com.example.i_prep.domain.use_cases

import com.example.i_prep.data.local.model.PTest
import com.example.i_prep.data.local.model.THistory
import com.example.i_prep.domain.repository.Repository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllHistory @Inject constructor(private val repository: Repository) {
    operator fun invoke(): Flow<List<THistory>> = repository.getAllHistory()
}