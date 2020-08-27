package com.bjornfree.tochka.loyalty.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

data class Events(
    @SerializedName("Events")
    val list: List<EventsImagesItem>
)

@Entity
data class EventsImagesItem(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @SerializedName("Name")
    val name: String,
    @SerializedName("Url")
    val imageUrl: String
)
