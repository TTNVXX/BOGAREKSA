package com.bogareksa.ui.pembeli.data.remote

import retrofit2.Call
import retrofit2.http.GET

interface ApiService {
    @GET("all-products")
    fun getProducts(): Call<List<ProductItem>>
}