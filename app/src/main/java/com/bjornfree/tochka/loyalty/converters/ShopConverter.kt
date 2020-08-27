package com.bjornfree.tochka.loyalty.converters

import androidx.room.TypeConverter
import com.bjornfree.tochka.loyalty.model.Shop
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import java.util.*

class ShopConverter {
    @TypeConverter
    fun fromString(value: String?): ArrayList<Shop> {
        val listType: Type = object : TypeToken<ArrayList<Shop?>?>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromArrayList(list: ArrayList<Shop?>?): String {
        val gson = Gson()
        return gson.toJson(list)
    }
}