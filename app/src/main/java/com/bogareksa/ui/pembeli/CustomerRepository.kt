package com.bogareksa.ui.pembeli

import android.app.Application
import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bogareksa.ui.pembeli.data.local.CartDao
import com.bogareksa.ui.pembeli.data.local.CartDatabase
import com.bogareksa.ui.pembeli.data.local.CartEntity
import com.bogareksa.ui.pembeli.data.remote.ApiConfig
import com.bogareksa.ui.pembeli.data.remote.ApiService
import com.bogareksa.ui.pembeli.data.remote.ProductResponse
import com.bogareksa.ui.pembeli.data.remote.ProductItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CustomerRepository(application: Application) {
    private val apiService: ApiService
    private val cartDao: CartDao

    init{
        val db = CartDatabase.getInstance(application)
        cartDao = db.cartDAO()
        apiService = ApiConfig.getApiService()
    }

    fun getAllProduct(): LiveData<List<ProductItem>> {
        val liveData = MutableLiveData<List<ProductItem>>()

        apiService.getProducts().enqueue(object : Callback<List<ProductItem>> {
            override fun onResponse(
                call: Call<List<ProductItem>>,
                response: Response<List<ProductItem>>
            ) {
                if (response.isSuccessful) {
                    liveData.value = (response.body()?: emptyList())
                }
            }
            override fun onFailure(call: Call<List<ProductItem>>, t: Throwable) {
                Log.e(ContentValues.TAG, "onFailure: ${t.message.toString()}")
            }
        })
        return liveData
    }

    fun searchProduct(query: String): LiveData<List<ProductResponse>> {
        val liveData = MutableLiveData<List<ProductResponse>>()

        apiService.getProducts().enqueue(object : Callback<List<ProductItem>> {
            override fun onResponse(call: Call<List<ProductItem>>, response: Response<List<ProductItem>>) {
                if (response.isSuccessful) {
                    val filteredList = response.body()?.filter {
                        it.name?.contains(query, ignoreCase = true) == true
                    }
                    liveData.value = listOf(ProductResponse(productList = filteredList))
                }
            }
            override fun onFailure(call: Call<List<ProductItem>>, t: Throwable) {
                Log.e(ContentValues.TAG, "onFailure: ${t.message.toString()}")
            }
        })
        return liveData
    }

    fun getCartList(): Flow<List<CartEntity>> {
        return cartDao.getAllCartItems()
    }

    suspend fun addToCart(
        imageUrl: String,
        name: String,
        amount: Int,
        totalPrice: Int
    ) = withContext(Dispatchers.IO) {
        val orderProductEntity = CartEntity(
            imageUrl = imageUrl,
            name = name,
            price = totalPrice,
            amount = amount
        )
        cartDao.addToCart(orderProductEntity)
    }


    fun delete(cart: CartEntity){
        cartDao.delete(cart)
    }

    companion object {
        @Volatile
        private var instance: CustomerRepository? = null
        fun getInstance(application: Application): CustomerRepository =
            instance ?: synchronized(this) {
                CustomerRepository(application).apply {
                    instance = this
                }
            }
    }
}