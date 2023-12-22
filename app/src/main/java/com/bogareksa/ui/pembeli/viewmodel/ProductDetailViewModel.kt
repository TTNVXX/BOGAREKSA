package com.bogareksa.ui.pembeli.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bogareksa.ui.pembeli.CustomerRepository
import com.bogareksa.ui.pembeli.data.remote.ProductItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProductDetailViewModel(private val repository: CustomerRepository) : ViewModel() {

//    fun addToCart(product: ProductItem, amount: Int, totalPrice: Int) {
//        viewModelScope.launch {
//            withContext(Dispatchers.IO){
//                product.productId?.let { productId ->
//                    val imageUrl = product.imageUrl ?: ""
//                    val name = product.name ?: ""
//                    val price = product.price ?: 0
//                    repository.addToCart(
//                        productId = productId,
//                        imageUrl = imageUrl,
//                        name = name,
//                        price = price,
//                        amount = amount,
//                        totalPrice = totalPrice
//                    )
//                }
//            }
//        }
//    }

    fun addToCart(imageUrl: String, name: String, amount: Int, totalPrice: Int) {
        viewModelScope.launch {
            repository.addToCart(imageUrl, name, amount, totalPrice)
        }
    }
}