package com.bjornfree.tochka.loyalty.data.retrofit

import com.bjornfree.tochka.loyalty.model.*
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MyApi {
    @GET(value = "Prices/request/Shops")
    suspend fun getShops(): Response<List<City>>

    @GET(value = "Prices/request/Events")
    suspend fun getEventImages(): Response<Events>

    @GET(value = "Prices/request/Products1")
    suspend fun getProducts(): Response<Products>

    @GET(value = "Prices/request/SearchByBarcode")
    suspend fun getArticleByBarcode(@Query("BarCode",encoded=true) barcode : String): Response<ProductArt>

    @GET(value = "Prices/request/Categories/")
    suspend fun getCategories(): Response<ResponseCategory>
}
