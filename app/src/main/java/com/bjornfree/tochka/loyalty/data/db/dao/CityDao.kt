package com.bjornfree.tochka.loyalty.data.db.dao

import androidx.room.*
import com.bjornfree.tochka.loyalty.model.City

@Dao
interface CityDao {

    @get:Query("SELECT * FROM City")
    val getAll: List<City>?

    @Insert
    fun addCity(city : City)

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addAllCity(cityList: List<City>)

    @Update
    fun update(city: City)

    @Delete
    fun delete(city: City)


    @Query("DELETE FROM City")
    fun deleteAll()
}