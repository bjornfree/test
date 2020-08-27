package com.bjornfree.tochka.loyalty.converters

import androidx.room.TypeConverter
import com.bjornfree.tochka.loyalty.model.CategoryItem
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import java.util.*

class CategoryConverter {
    @TypeConverter
    fun fromString(value: String?): ArrayList<CategoryItem> {
        val listType: Type = object : TypeToken<ArrayList<CategoryItem?>?>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromArrayList(list: ArrayList<CategoryItem?>?): String {
        val gson = Gson()
        return gson.toJson(list)
    }
}