package com.example.i_prep.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.randomboiii.i_prep.data.Question

@Entity(tableName = "history")
data class THistory(
    @PrimaryKey(autoGenerate = true) val historyId: Int = 0,
    @ColumnInfo(name = "testId") val testId: Int,
    @ColumnInfo(name = "questions") val questions: List<Question>,
    @ColumnInfo(name = "correctAnswer") val selectedAnswer: List<String>,
    @ColumnInfo(name = "questionsTaken") val questionsTaken: Int,
    @ColumnInfo(name = "score") val score: Int,
    @ColumnInfo(name = "dateTaken") val dateTaken: Long,
)
