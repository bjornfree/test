package com.bjornfree.tochka.loyalty.data.repository

import android.content.Context
import androidx.sqlite.db.SimpleSQLiteQuery
import com.bjornfree.prices.data.retrofit.SafeApiRequest
import com.bjornfree.prices.utils.ApiException
import com.bjornfree.prices.utils.NoInternetException
import com.bjornfree.tochka.loyalty.data.db.AppDatabase
import com.bjornfree.tochka.loyalty.data.retrofit.ApiWorker
import com.bjornfree.tochka.loyalty.model.*


class Repository(private val context: Context) : SafeApiRequest() {
    private val db = AppDatabase.getAppDataBase(context)!!

    @Throws(NoInternetException::class, ApiException::class)
    suspend fun loadShops() {
        val response = apiRequest { ApiWorker.getApi(context).getShops() }
        val shopList = response as ArrayList<City>
        db.shopsDao().deleteAll()
        db.shopsDao().addAllCity(shopList)
    }

    @Throws(NoInternetException::class, ApiException::class)
    suspend fun loadProducts() {
        val response = apiRequest { ApiWorker.getApi(context).getProducts() }
        val products = response.list
        db.productsDao().deleteAll()
        db.productsDao().addAllProducts(products)
    }

    @Throws(ApiException::class)
    suspend fun loadEvent() {
        val list = apiRequest { ApiWorker.getApi(context).getEventImages() }
        db.eventDao().deleteAll()
        db.eventDao().addAll(list.list)
    }

    @Throws(ApiException::class)
    suspend fun loadCategories() {
        val list = apiRequest { ApiWorker.getApi(context).getCategories() }
        db.categoryDao().deleteAll()
        db.categoryDao().addAllCategory(list.types)
    }


    fun getShopsFromDB(): List<City>? {
        return db.shopsDao().getAll
    }

    fun getEventFromDB(): List<EventsImagesItem>? {
        return db.eventDao().getAll
    }

    fun getProductsFromDB(): List<Product>? {
        return db.productsDao().getAll
    }

    fun getProductsFromDbByCategory(categoryName : String): List<Product>? {
        return db.productsDao().getProductsByCategory(categoryName)
    }

    fun getCategoriesFromDB(): List<Category>? {
        return db.categoryDao().getAllCategories
    }

    @Throws(NoInternetException::class, ApiException::class)
    suspend fun loadArticleByBarcode(barcode : String) : ProductArt {
        return apiRequest { ApiWorker.getApi(context).getArticleByBarcode(barcode) }
    }

    fun getProductByArticle(art : String): Product? {
        return db.productsDao().getProductByArt(art)
    }

    fun searchByName(name : String): List<Product>? {
        return db.productsDao().searchByName(name)?.let {  it.sortedBy { product -> product.isExclusive } }
    }

    fun searchByCustomQuery(query : SimpleSQLiteQuery): List<Product>? {
        return db.productsDao().customSearch(query)?.let {  it.sortedBy { product -> product.isExclusive } }
    }

}