package com.bogareksa.ui.pembeli.state

import com.bogareksa.ui.pembeli.data.local.CartEntity

data class CartState(
    val orderProducts: List<CartEntity>,
    val totalPrice: Int,
)