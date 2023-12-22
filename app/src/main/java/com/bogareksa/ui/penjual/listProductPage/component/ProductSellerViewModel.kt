package com.bogareksa.ui.penjual.listProductPage.component

import android.content.ContentValues.TAG
import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bogareksa.io.response.MyProductsItem
import com.bogareksa.io.response.ResponseProducts
import com.bogareksa.io.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductSellerViewModel : ViewModel(){
    private var _listProduct = MutableLiveData<List<MyProductsItem>>()
    var listProducts: LiveData<List<MyProductsItem>> = _listProduct

    private var _detailProduct = MutableLiveData<MyProductsItem>()
    var detailProducts: LiveData<MyProductsItem> = _detailProduct

    private var _isFetch = MutableLiveData<Boolean>(false)
    var isFetch: LiveData<Boolean> = _isFetch


    fun findProductById(id : String){
        Log.d("dataList seller",_listProduct.value.toString())
        _detailProduct.value =  _listProduct.value!!.find { items ->
            items.productId == id
        }
    }

    fun findProducts(token : String) {
        Log.d("mulai cari product","cari product cuy")
        val client = ApiConfig.getApiService().getUserData(token)
        client.enqueue(object : Callback<ResponseProducts> {
            override fun onResponse(
                call: Call<ResponseProducts>,
                response: Response<ResponseProducts>
            ) {

                if (response.isSuccessful) {
                    _listProduct.value = response.body()?.myProducts
                    _isFetch.value = true
//                    Log.d("berhasil respons product","success product cuy ${listProducts.value!![0].name}")
                    Log.d("print all the fetched product", response.body()?.myProducts.toString())

                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ResponseProducts>, t: Throwable) {

                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }
}