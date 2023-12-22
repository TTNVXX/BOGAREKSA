package com.bogareksa.ui.pembeli.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bogareksa.ui.pembeli.CustomerRepository
import com.bogareksa.ui.pembeli.data.local.CartEntity
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class CartViewModel (val repository: CustomerRepository) : ViewModel(){

    private val _cartList = MutableLiveData<List<CartEntity>>()
    val cartList: LiveData<List<CartEntity>> = _cartList


    fun deleteCart(idx : Int){

        cartList.value?.let {
            repository.delete(it[idx])
        }

    }

    fun getCartList() {
        viewModelScope.launch {
            repository.getCartList().collect{
                _cartList.value = it
            }
        }
    }
}