package com.bogareksa.ui.penjual.order

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import com.bogareksa.MainActivity
import com.bogareksa.ui.auth.LoginActivity
import com.bogareksa.ui.pembeli.components.TransactionItem
import com.bogareksa.ui.pembeli.data.local.CartEntity
import com.bogareksa.ui.pembeli.viewmodel.CartViewModel
import com.bogareksa.ui.penjual.addProductPage.AddProductActivity
import com.bogareksa.ui.penjual.addProductPage.component.AddProductViewModel
import com.bogareksa.ui.penjual.homePage.component.CustomButton
import com.bogareksa.ui.penjual.mainSellerComponent.AppbarImgBackground



class NotificationSellerActivity : AppCompatActivity() {

//    private lateinit var orderVm : CartViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//
//        orderVm = ViewModelProvider(this,ViewModelProvider.NewInstanceFactory())[CartViewModel::class.java]
//        orderVm.getCartList()
        setContent {
            NotificationSellerContent(navBack = {onBackPressed()})
        }
//        orderVm.cartList.observe(this){
//            if(it.isEmpty()){
//                setContent {
//                    Scaffold(
//                        topBar = {
//                            AppbarImgBackground(navBack = { onBackPressed()}, title = "Settings")
//                        }
//                    ){
//                        Box(Modifier.padding(it)){
//
//                        }
//                    }
//                }
//            }else{
//                setContent {
//                    NotificationSellerContent(data = it,navBack = {onBackPressed()})
//                }
//            }
//        }

    }
}



@Composable
fun NotificationSellerContent(navBack : () -> Unit){
    val activityResultLauncher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        // Handle the result if needed
        if (result.resultCode == Activity.RESULT_OK) {
            // Handle success
        } else {
            // Handle failure or canceled
        }
    }
    val ctx = LocalContext.current
    Scaffold(
        topBar = {
            AppbarImgBackground(navBack = { navBack()}, title = "Settings")
        }
    ){
        Column(modifier = Modifier
            .padding(it)
            .fillMaxWidth()){
          Button(
              colors = ButtonColors(containerColor = Color(0xff00698C), disabledContentColor = Color(0xff00698C), contentColor = Color.White, disabledContainerColor = Color(0xff00698C)),
              shape = MaterialTheme.shapes.medium,
              modifier = Modifier
                  .fillMaxWidth()
                  .padding(20.dp)
              ,onClick = {
              activityResultLauncher.launch(
                  Intent(ctx, LoginActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
              )
          }) {
              Text(text = "LogOut ->")
          }

//            LazyColumn(
//                verticalArrangement = Arrangement.spacedBy(16.dp),
//                modifier = Modifier
//                    .weight(1f)
//                    .padding(horizontal = 8.dp)
//            ) {
//                itemsIndexed(data.orEmpty()) {idx,cart ->
//                    TransactionItem(
////                        vm= viewModel,
//                        data = cart,
//                        index = idx)
//                }
//            }

        }
    }
}
