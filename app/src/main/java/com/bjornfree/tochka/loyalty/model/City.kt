package com.bjornfree.tochka.loyalty.model

import android.os.Parcelable
import androidx.room.*
import com.bjornfree.tochka.loyalty.converters.ShopConverter
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

@Parcelize
@Entity
data class City(
    @PrimaryKey
    @SerializedName("Name")
    val name: String,
    @TypeConverters(ShopConverter::class)
    @SerializedName("Shops")
    val shops: ArrayList<Shop>?
) : Parcelable


@Parcelize
@Entity
data class Shop(
    @PrimaryKey
    @SerializedName("Name")
    val name: String,
    @SerializedName("Latitude")
    val latitude: String,
    @SerializedName("Longitude")
    val longitude: String,
    @SerializedName("address")
    val address: String,
    @SerializedName("schedule")
    val schedule: String,
    @SerializedName("Phone")
    val storePhoneNumber: String,
    @SerializedName("BusStop")
    val busStop: String,
    @SerializedName("Image")
    val image: String
) : Parcelable, Serializable


