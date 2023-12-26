package com.example.i_prep.domain.use_cases

import com.example.i_prep.data.local.model.PTest
import com.example.i_prep.domain.repository.Repository
import javax.inject.Inject

class UpsertTest @Inject constructor(private val repository: Repository) {
    suspend operator fun invoke(pTest: PTest) = repository.upsertTest(pTest)
}