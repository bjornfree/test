package com.bjornfree.tochka.loyalty.data.retrofit

import android.content.Context
import com.bjornfree.prices.utils.BasicAuthInterceptor
import com.bjornfree.prices.utils.defaultPrefs
import com.bjornfree.prices.utils.get
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

object ApiWorker {
    private var mClient: OkHttpClient? = null
    private const val username = "alcohol"
    private const val password = "alcohol"

    private val interceptor : HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
        this.level = HttpLoggingInterceptor.Level.BODY
    }

    fun getApi(context: Context): MyApi {
        val serverAddress = "c3.tochka.wine:4545/TOCHKA-UT/hs/"

        return Retrofit.Builder()
            .baseUrl("http://${serverAddress}/")
            .addConverterFactory(GsonConverterFactory.create())
            .addConverterFactory(ScalarsConverterFactory.create())
            .client(buildClient(context))
            .build()
            .create(MyApi::class.java)
    }

    fun postApi(context: Context): MyApi {
        val serverAddress = defaultPrefs(context).get("server", "")

        return Retrofit.Builder()
            .baseUrl("http://${serverAddress}/")
            .addConverterFactory(GsonConverterFactory.create())
            .addConverterFactory(ScalarsConverterFactory.create())
            .client(buildClient(context))
            .build()
            .create(MyApi::class.java)
    }

    private fun buildClient(context: Context): OkHttpClient {
        val timeOut = Integer.parseInt(defaultPrefs(context).get("timeout", "5")).toLong()
        val httpBuilder = OkHttpClient.Builder()
        httpBuilder
            .connectTimeout(timeOut, TimeUnit.SECONDS)
            .readTimeout(timeOut, TimeUnit.SECONDS)
            .addInterceptor(BasicAuthInterceptor(username, password))
            .addInterceptor(NetworkConnectionInterceptor(context))
            .addInterceptor(interceptor)
        mClient = httpBuilder.build()

        return mClient!!
    }
}