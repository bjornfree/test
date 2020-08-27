package com.bjornfree.tochka.loyalty.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Entity
data class Products(
    @SerializedName("Products")
    val list: List<Product>
)

@Entity
data class Product(
    @PrimaryKey
    @SerializedName("Art")
    val art: String,
    @SerializedName("Brand")
    val brand: String,
    @SerializedName("Country")
    val country: String,
    @SerializedName("Description")
    val description: String,
    @SerializedName("FullName")
    val fullName: String,
    @SerializedName("Name")
    val name: String,
    @SerializedName("Price")
    val price: Double?,
    @SerializedName("Producer")
    val producer: String,
    @SerializedName("Rating")
    val rating: Double?,
    @SerializedName("Type")
    val type: String,
    @SerializedName("Category")
    val category: String,
    @SerializedName("Strength")
    val strength: String,
    @SerializedName("Volume")
    val volume: String,
    @SerializedName("New")
    val isNew: Boolean,
    @SerializedName("Exclusive")
    val isExclusive: Boolean,
    @SerializedName("Event")
    val isEvent: Boolean,
    @SerializedName("OldPrice")
    val oldPrice: String
) : Serializable