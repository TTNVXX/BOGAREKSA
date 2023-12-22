package com.bogareksa.ui.navigation

sealed class Screen(val route: String){
    object AddProductSeller : Screen("addProductSeller")
    object HomePageSeller : Screen("homePageSeller")
    object getImageSeller : Screen("getImageSeller")
    object DetailProductSeller : Screen("detailProductSeller/{productId}/{productName}/{productPrice}/{productImage}"){
        fun createRoute(
            productId:String,
            productName:String,
            productPrice:String,
            productImage: String,
//            productDetail: String,

        ) = "detailProductSeller/$productId/$productName/$productPrice/$productImage"
    }
    object EditDetailProduct : Screen("detailDetailProduct")
    object ListSellerProduct : Screen("ListSellerProduct")

    object UploadImage : Screen("UploadImageActivity")

    object ListProduct : Screen("ListProducts")



}