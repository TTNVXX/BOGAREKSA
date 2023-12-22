package com.bogareksa.ui.pembeli.navigation

sealed class Screen(val route: String) {
    object ProductList : Screen("home")
    object CartList : Screen("cart")
    object CustomerProfile : Screen("profile")
    object ProductDetail : Screen("detail/{productId}") {
        fun createRoute(productId: String) = "detail/$productId"
    }
}

