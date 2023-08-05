package com.example.jumpingminds_assignments.db

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {

    @TypeConverter
    fun fromString(value: String): List<Any> {
        val listType = object : TypeToken<List<Any>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun toString(list: List<Any>): String {
        return Gson().toJson(list)
    }
}