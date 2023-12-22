package com.bogareksa.ui.penjual.detailProductPage.component

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bogareksa.io.response.MyProductsItem
import com.bogareksa.io.response.ResponseDeleteProduct
import com.bogareksa.io.response.ResponseProducts
import com.bogareksa.io.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetaiProductSellerlViewModel : ViewModel(){
    private var _detailProduct = MutableLiveData<MyProductsItem>()
    var detailProducts: LiveData<MyProductsItem> = _detailProduct

    fun deletedProduct(token : String,productId : String) {
        Log.d("mulai cari product id","cari product cuy by id")
        val client = ApiConfig.getApiService().deleteProduct(token,productId)
        client.enqueue(object : Callback<ResponseDeleteProduct> {
            override fun onResponse(
                call: Call<ResponseDeleteProduct>,
                response: Response<ResponseDeleteProduct>
            ) {

                if (response.isSuccessful) {
                    Log.d("berhasil delete product","success delete product cuy id:$productId")

                } else {
                    Log.e(ContentValues.TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ResponseDeleteProduct>, t: Throwable) {

                Log.e(ContentValues.TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

}