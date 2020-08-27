package com.bjornfree.tochka.loyalty.ui.home

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.bjornfree.prices.utils.ApiException
import com.bjornfree.prices.utils.Coroutines
import com.bjornfree.prices.utils.NoInternetException
import com.bjornfree.tochka.loyalty.data.repository.Repository
import com.bjornfree.tochka.loyalty.model.Product
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.net.SocketException

class MainViewModel(application: Application) : AndroidViewModel(application) {
    var mainListener: MainListener? = null
    private val repository = Repository(application.applicationContext)
    private val mContext = application.applicationContext
    val imagesLiveData: MutableLiveData<MutableList<String>> = MutableLiveData()
    val productsLiveData: MutableLiveData<MutableList<Product>> = MutableLiveData()

    init {
        getEvents()
        getProducts()
    }

    fun loadMainData() = viewModelScope.launch(Dispatchers.IO) {
        try {
            repository.loadShops()
            repository.loadEvent()
            repository.loadProducts()
            repository.loadCategories()
            Coroutines.main {
                delay(1000)
                mainListener?.onSuccess()
            }
        } catch (e: ApiException) {
            Log.e("ERROR", e.message!!)
        } catch (e: NoInternetException) {
            Log.e("ERROR", e.message!!)
        } catch (e: SocketException) {
            Log.e("ERROR", e.message!!)
        }
    }

     fun getEvents() {
        try {
            viewModelScope.launch(Dispatchers.IO) {
                val images = repository.getEventFromDB()
                val list = ArrayList<String>()
                images!!.forEach { list.add(it.imageUrl) }
                Coroutines.main {
                    imagesLiveData.value = list
                }
            }
        } catch (e: ApiException) {
            Log.e("ERROR", e.message!!)
        } catch (e: NoInternetException) {
            Log.e("ERROR", e.message!!)
        } catch (e: SocketException) {
            Log.e("ERROR", e.message!!)
        }
    }

     fun getProducts() {
        try {
            viewModelScope.launch(Dispatchers.IO) {
                val products = repository.getProductsFromDB()
                Coroutines.main {
                    productsLiveData.value = products as ArrayList
                }
            }
        } catch (e: ApiException) {
            Log.e("ERROR", e.message!!)
        } catch (e: NoInternetException) {
            Log.e("ERROR", e.message!!)
        } catch (e: SocketException) {
            Log.e("ERROR", e.message!!)
        }
    }
}