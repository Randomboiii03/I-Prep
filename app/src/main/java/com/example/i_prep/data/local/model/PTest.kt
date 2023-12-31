package com.example.i_prep.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.i_prep.domain.api.model.dto.Question

@Entity(tableName = "pTest")
data class PTest(
    @PrimaryKey(autoGenerate = true) val testId: Int = 0,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "tags") val tags: List<String>,
    @ColumnInfo(name = "questionType") val questionType: String,
    @ColumnInfo(name = "questions") val questions: List<Question>,
    @ColumnInfo(name = "totalItems") val totalItems: Int,
    @ColumnInfo(name = "language") val language: String,
    @ColumnInfo(name = "reference") val reference: String,
    @ColumnInfo(name = "image") val image: String,
    @ColumnInfo(name = "dateCreated") val dateCreated: Long,
    @ColumnInfo(name = "isTimed") val isTimed: Boolean = true,
    @ColumnInfo(name = "itemSet") val itemSet: Int = 20,
    @ColumnInfo(name = "isAvailable") val isAvailable: Boolean = true
) {
    fun doesMatchQuery(query: String): Boolean {
        val matchingCombinations = listOf(
            title,
            description,
            *tags.toTypedArray(),
            language,
            reference
        )

        return matchingCombinations.any { it.lowercase().contains(query) }
    }
}
