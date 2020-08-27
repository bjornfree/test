package com.bjornfree.tochka.loyalty.data.db.dao

import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery
import com.bjornfree.tochka.loyalty.model.City
import com.bjornfree.tochka.loyalty.model.Product

@Dao
interface ProductDao {

    @get:Query("SELECT * FROM Product")
    val getAll: List<Product>?


    @Insert
    fun addProduct(product: Product)

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addAllProducts(productList: List<Product>)

    @Update
    fun update(product: Product)

    @Delete
    fun delete(product: Product)

    @Query("DELETE FROM Product")
    fun deleteAll()

    @Query("SELECT * FROM Product WHERE Product.category = :category ORDER BY Product.name")
    fun getProductsByCategory(category : String) : List<Product>?

    @Query("SELECT * FROM Product WHERE Product.art = :art")
    fun getProductByArt(art : String) : Product?

    @Query("SELECT * FROM Product WHERE Product.name LIKE '%' || :name  || '%'")
    fun searchByName(name : String) : List<Product>?

    @RawQuery
    fun customSearch(query : SupportSQLiteQuery) : List<Product>?

}