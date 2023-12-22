package com.bogareksa.ui.pembeli

import android.app.Activity
import android.app.Application
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.bogareksa.MainActivity
import com.bogareksa.ui.pembeli.navigation.NavigationItem
import com.bogareksa.ui.pembeli.navigation.Screen
import com.bogareksa.ui.pembeli.screen.CartList
import com.bogareksa.ui.pembeli.screen.CustomerProfile
import com.bogareksa.ui.pembeli.screen.ProductDetailActivity
import com.bogareksa.ui.pembeli.screen.ProductList
import com.bogareksa.ui.pembeli.viewmodel.CartViewModel
import com.bogareksa.ui.pembeli.viewmodel.ProductListViewModel





@Composable
fun BogareksaCustomerApp(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
){
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val context = LocalContext.current
    val customerRepository = remember {
        CustomerRepository.getInstance(context.applicationContext as Application)
    }
    val productListViewModel = remember {
        ProductListViewModel(customerRepository)
    }
    val cartViewModel = remember {
        CartViewModel(customerRepository)
    }
    val activityResultLauncher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
        }
        else {
        }
    }

    Scaffold(
        bottomBar = {
            if (currentRoute != Screen.ProductDetail.route) {
                BottomBar(navController)
            }
        },
        modifier = modifier
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.ProductList.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.ProductList.route) {
                ProductList(
                    viewModel = productListViewModel,
                    modifier = modifier,
                    navigateToDetail = { name,image,price,desc,seller,date ->
                        activityResultLauncher.launch(
                            Intent(context, ProductDetailActivity::class.java)
                                .putExtra("name",name)
                                .putExtra("image",image)
                                .putExtra("price",price)
                                .putExtra("desc",desc)
                                .putExtra("seller",seller)
                                .putExtra("date",date)
                        )
                    }
                )
            }
            composable(Screen.CartList.route) {
                CartList(
                    viewModel = cartViewModel)
            }
            composable(Screen.CustomerProfile.route) {
                CustomerProfile(
                    onBackClick = {
                        activityResultLauncher.launch(
                           Intent(context,MainActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

                        )
                    }
                )
            }
        }
    }
}

@Composable
private fun BottomBar(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier,
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        val navigationItems = listOf(
            NavigationItem(
                icon = Icons.Default.Home,
                screen = Screen.ProductList
            ),
            NavigationItem(
                icon = Icons.Default.List,
                screen = Screen.CartList
            ),
            NavigationItem(
                icon = Icons.Default.Person,
                screen = Screen.CustomerProfile
            ),
        )
        navigationItems.map { item ->
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = null,
                        tint = Color(0xFF00698C)
                    )
                },
                selected = currentRoute == item.screen.route,
                onClick = {
                    navController.navigate(item.screen.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        restoreState = true
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BogareksaCustomerAppPreview() {
    MaterialTheme {
        BogareksaCustomerApp()
    }
}