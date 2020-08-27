package com.bjornfree.tochka.loyalty.data.db.dao

import androidx.room.*
import com.bjornfree.tochka.loyalty.model.EventsImagesItem

@Dao
interface EventDao {

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addAll(list : List<EventsImagesItem>)

    @get:Query("SELECT * FROM EventsImagesItem")
    val getAll: List<EventsImagesItem>?


    @Query("DELETE FROM EventsImagesItem")
    fun deleteAll()
}