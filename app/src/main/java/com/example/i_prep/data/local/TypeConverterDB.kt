package com.example.i_prep.data.local

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.example.i_prep.domain.api.model.dto.Question

class TypeConverterDB {
    @TypeConverter
    fun fromString(value: String?): List<String>? {
        return value?.split(",")
    }

    @TypeConverter
    fun toString(value: List<String>?): String? {
        return value?.joinToString(",")
    }

    @TypeConverter
    fun fromInt(value: String?): List<Int>? {
        return value?.split(",")?.map { it.toInt() }
    }

    @TypeConverter
    fun toInt(value: List<Int>?): String? {
        return value?.joinToString(",")
    }

    private val gson = Gson()

    @TypeConverter
    fun fromJson(json: String?): List<Question>? {
        if (json == null) {
            return emptyList()
        }

        val listType = object : TypeToken<List<Question>>() {}.type
        return gson.fromJson(json, listType)
    }

    @TypeConverter
    fun toJson(data: List<Question>?): String? {
        return gson.toJson(data)
    }
}