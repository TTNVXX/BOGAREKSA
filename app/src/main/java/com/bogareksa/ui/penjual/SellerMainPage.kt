package com.bogareksa.ui.penjual

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.activity
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.bogareksa.sessions.LoginSession
import com.bogareksa.ui.auth.component.LoginViewModel
import com.bogareksa.ui.navigation.Screen
import com.bogareksa.ui.pembeli.CustomerRepository
import com.bogareksa.ui.pembeli.viewmodel.CartViewModel
import com.bogareksa.ui.penjual.addProductPage.AddProductActivity
//import com.bogareksa.ui.penjual.addProductPage.AddProductPageSeller
import com.bogareksa.ui.penjual.addProductPage.component.AddProductViewModel
import com.bogareksa.ui.penjual.detailProductFromList.DetailFromListActivity
import com.bogareksa.ui.penjual.detailProductPage.DetailProductSellerPage
import com.bogareksa.ui.penjual.detailProductPage.component.DetaiProductSellerlViewModel
import com.bogareksa.ui.penjual.editDetailProductSellerPage.EditDetailProduct
import com.bogareksa.ui.penjual.getImgPage.GetImgPage
import com.bogareksa.ui.penjual.homePage.HomePageSeller
import com.bogareksa.ui.penjual.listProductPage.ListSellerProductPage
import com.bogareksa.ui.penjual.listProductPage.component.ProductListActivity
import com.bogareksa.ui.penjual.listProductPage.component.ProductSellerViewModel
import com.bogareksa.ui.penjual.order.NotificationSellerActivity


@SuppressLint("SuspiciousIndentation")
@Composable
fun SellerMainPage(email : String) {
    val navController = rememberNavController()

    //VIEWMODEL
    val productViewModel = ProductSellerViewModel()
    val loginViewModel = LoginViewModel()
    val detailViewModel = DetaiProductSellerlViewModel()
    val productAddViewModel = AddProductViewModel()


    val context = LocalContext.current
    val navBackStakEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStakEntry?.destination?.route

    val customerRepository = remember {
        CustomerRepository.getInstance(context.applicationContext as Application)
    }

    val cartViewModel = remember {
        CartViewModel(customerRepository)
    }

    //TOKEN
    val session = LoginSession(ctx = LocalContext.current)
    var user: HashMap<String,String> = session.getUserProduct()
    val castToTxt = user.toString()
    val theToken = castToTxt.substringAfter("{token=").substringBefore("}")


    val activityResultLauncher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            // Handle the result if needed
            if (result.resultCode == Activity.RESULT_OK) {
                // Handle success
            } else {
                // Handle failure or canceled
            }
        }


        NavHost(navController = navController, startDestination = Screen.HomePageSeller.route ){
            composable(Screen.HomePageSeller.route){
                HomePageSeller(
                    orderVm = cartViewModel,
                    email = email,
                    getAddPageRoute = {
//                        navController.navigate(Screen.AddProductSeller.route)
                        activityResultLauncher.launch(
                            Intent(context, AddProductActivity::class.java)
                        )
                    },
//                    toTheDetail = {
//                        navController.navigate(Screen.DetailProductSeller.route)
//                    },
                    toTheListProduct = {
                        navController.navigate(Screen.ListSellerProduct.route)
//                        activityResultLauncher.launch(
//                            Intent(context, ProductListActivity::class.java)
//                        )
                    },
                    vm = productViewModel,
                    navCrontroller = navController,
                    toTheNotification = {
                        activityResultLauncher.launch(
                            Intent(context, NotificationSellerActivity::class.java)
                        )
                    }
//                    toTheDetail = {
//
//                    }
                )
            }

//            composable(Screen.AddProductSeller.route){
//                AddProductPageSeller(
//                    navBack = {navController.navigateUp()},
//                    toTheGetImg = {
//                        activityResultLauncher.launch(
//                            Intent(context, UploadImageActivity::class.java)
//                        )
//                    },
//                    token = theToken,
//                    vm = productAddViewModel
//                )
//            }

            composable(Screen.getImageSeller.route){
                GetImgPage(
                    navBack = {navController.popBackStack()}
                )
            }


            composable(
                route = Screen.DetailProductSeller.route,
                arguments = listOf(
                    navArgument("productId"){type = NavType.StringType},
                    navArgument("productName"){type = NavType.StringType},
                    navArgument("productPrice"){type = NavType.StringType},
                    navArgument("productImage"){type = NavType.StringType},
//                    navArgument("productDetail"){type = NavType.StringType},

                )
            ){
                val id = it.arguments?.getString("productId") ?: ""
                val name = it.arguments?.getString("productName") ?: ""
                val price = it.arguments?.getString("productPrice") ?: ""
                val img = it.arguments?.getString("productImage") ?: ""
//                val detail = it.arguments?.getString("productDetail") ?: ""
                DetailProductSellerPage(
                    token = theToken,
                    id = id,
                    navBack = {navController.popBackStack()},
                    vm = productViewModel,
                    price = price,
                    name = name,
                    img = img,
                    vmDetail = detailViewModel
//                    detail = detail
                )
            }

            composable(Screen.EditDetailProduct.route){
                EditDetailProduct()
            }

            composable(Screen.ListSellerProduct.route){
                ListSellerProductPage(
                    navBack = {
                        navController.popBackStack()
                              },
                    vm = productViewModel,
                    toDetail = {product->
                        activityResultLauncher.launch(
                            Intent(context, DetailFromListActivity::class.java)
                                .putExtra("data",product)
                        )
                    },
                    navControll = navController

                )
            }

            activity(Screen.UploadImage.route){

//                UploadImageActivity()
            }

            activity(Screen.ListProduct.route){
                ProductListActivity()
            }



    }
}





