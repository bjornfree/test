package com.bjornfree.tochka.loyalty.ui.shops

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.bjornfree.prices.utils.ApiException
import com.bjornfree.prices.utils.Coroutines
import com.bjornfree.prices.utils.NoInternetException
import com.bjornfree.tochka.loyalty.data.repository.Repository
import com.bjornfree.tochka.loyalty.model.Shop
import kotlinx.coroutines.launch
import java.net.SocketException

class ShopViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = Repository(application.applicationContext)
    private val mContext = application.applicationContext

    val citiesLiveData: MutableLiveData<MutableList<ExpandableGroupCity>> = MutableLiveData()

    init {
        fetchCitiesList()
    }


    private fun fetchCitiesList() = viewModelScope.launch {
        Coroutines.io {
            try {
                val documentsList = repository.getShopsFromDB()
                documentsList?.let {
                    for (city in documentsList) {
                        city.shops!!.sortBy { it.name }
                    }
                    val expandedList =
                        documentsList.map {
                            ExpandableGroupCity(
                                it.name,
                                it.shops!! as List<Shop>
                            )
                        }
                    Coroutines.main {
                        citiesLiveData.value = expandedList as MutableList<ExpandableGroupCity>
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


}