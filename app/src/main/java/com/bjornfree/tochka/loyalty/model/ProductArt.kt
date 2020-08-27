package com.bjornfree.tochka.loyalty.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ProductArt(
	@field:SerializedName("Art")
	val art: String
) : Parcelable
