package com.bjornfree.tochka.loyalty.data.db.dao

import androidx.room.*
import com.bjornfree.tochka.loyalty.model.Category

@Dao
interface CategoryDao {

    @get:Query("SELECT * FROM Category")
    val getAllCategories: List<Category>?

    @Insert
    fun addCategory(category : Category)

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addAllCategory(category: List<Category>)

    @Update
    fun update(category: Category)

    @Delete
    fun delete(category: Category)


    @Query("DELETE FROM Category")
    fun deleteAll()
}