package com.bjornfree.tochka.loyalty.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.bjornfree.tochka.loyalty.converters.CategoryConverter
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

@Parcelize
data class ResponseCategory(
    @SerializedName("Types")
    val types: List<Category>
) : Parcelable

@Parcelize
@Entity
data class CategoryItem(
    @SerializedName("Name")
    val name: String,
    @SerializedName("Image")
    val image: String,
    @SerializedName("Fields")
    val fields: List<FieldsItem>?

) : Parcelable, Serializable

@Parcelize
@Entity
data class Category(
	@PrimaryKey
    @SerializedName("Type")
    val typeName: String,
    @TypeConverters(CategoryConverter::class)
    @SerializedName("Categories")
    val categoryItems: ArrayList<CategoryItem>?

) : Parcelable

@Parcelize
@Entity
data class FieldsItem(
    @SerializedName("Field")
    val field: String,
    @SerializedName("Name")
    val name: String
) : Parcelable, Serializable
