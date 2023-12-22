package com.bogareksa.ui.pembeli.viewmodel

import android.content.ContentValues
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bogareksa.ui.pembeli.CustomerRepository
import com.bogareksa.ui.pembeli.data.remote.ProductResponse
import com.bogareksa.ui.pembeli.data.remote.ProductItem
import kotlinx.coroutines.launch

class ProductListViewModel(private val repository: CustomerRepository) : ViewModel() {

    private val _productList = MutableLiveData<List<ProductItem>>()
    var productList: LiveData<List<ProductItem>> = _productList

    private val _searchResult = MutableLiveData<List<ProductResponse>>()
    val searchResult: LiveData<List<ProductResponse>> get() = _searchResult

    private val _query = mutableStateOf("")
    val query: State<String> get() = _query

    fun getProducts() {
        viewModelScope.launch {
            repository.getAllProduct().observeForever {
                _productList.value = it
            }
        }
    }

    fun search(newQuery: String) {
        viewModelScope.launch {
            _query.value = newQuery
            try {
                val result = repository.searchProduct(_query.value)
                _searchResult.value = result.value
            } catch (e: Exception) {
                Log.e(ContentValues.TAG, "Error searching products: ${e.message}")
            }
        }
    }
}