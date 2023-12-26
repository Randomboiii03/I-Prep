package com.example.i_prep.domain.use_cases

import com.example.i_prep.data.local.model.THistory
import com.example.i_prep.domain.repository.Repository
import javax.inject.Inject

class DeleteHistory @Inject constructor(private val repository: Repository) {
    suspend operator fun invoke(tHistory: THistory) = repository.deleteHistory(tHistory)
}