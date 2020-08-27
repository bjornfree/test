package com.bjornfree.tochka.loyalty.ui.catalog

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.bjornfree.prices.utils.ApiException
import com.bjornfree.prices.utils.Coroutines
import com.bjornfree.prices.utils.NoInternetException
import com.bjornfree.tochka.loyalty.data.repository.Repository
import com.bjornfree.tochka.loyalty.model.Category
import com.bjornfree.tochka.loyalty.model.Product
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.reflect.Field
import java.net.SocketException

class CatalogViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = Repository(application.applicationContext)
    val categoriesLiveData: MutableLiveData<MutableList<Category>> = MutableLiveData()
    val productList: MutableLiveData<MutableList<Product>> = MutableLiveData()
    val productArt: MutableLiveData<Product> = MutableLiveData()
    val fieldsEntity: MutableLiveData<HashSet<String>> = MutableLiveData()

    init {
        getCategories()
    }


    private fun getCategories() {
        try {
            viewModelScope.launch(Dispatchers.IO) {
                val categories = repository.getCategoriesFromDB()
                Coroutines.main {
                    categoriesLiveData.value = categories as MutableList<Category>?
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

    fun getProductsByCategoryName(categoryName: String) {
        try {
            viewModelScope.launch(Dispatchers.IO) {
                val products = repository.getProductsFromDbByCategory(categoryName)
                Coroutines.main {
                    productList.value = products as MutableList<Product>
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

    fun getProductByBarcode(barcode: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val art = repository.loadArticleByBarcode(barcode)
            val product = repository.getProductByArticle(art.art)
            Coroutines.main {
                productArt.value = product
            }
        }
    }

    fun searchByName(name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val listProducts = repository.searchByName(name)
            listProducts?.let {
                if (listProducts.isNotEmpty()) {
                    Coroutines.main {
                        productList.value = listProducts as MutableList<Product>
                    }
                }
            }
        }
    }

    fun getSearchFieldEntity(fieldString: String, productCategory: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val field: Field? = Product::class.java.getDeclaredField(fieldString)
            field?.let {
                val listProducts = repository.getProductsFromDbByCategory(productCategory)
                val entity = ArrayList<String>()
                listProducts?.let {
                    listProducts.forEach { product ->
                        field.isAccessible = true
                        entity.add(field[product].toString())
                    }
                    val uniqueValues: HashSet<String> = HashSet(entity)
                    Coroutines.main {
                        fieldsEntity.value = uniqueValues
                    }
                }
            }
        }
    }

}