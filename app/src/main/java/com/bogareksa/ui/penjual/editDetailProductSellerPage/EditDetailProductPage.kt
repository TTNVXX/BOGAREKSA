package com.bogareksa.ui.penjual.editDetailProductSellerPage

import android.provider.CalendarContract.Colors
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bogareksa.R
import com.bogareksa.ui.penjual.editDetailProductSellerPage.component.InputEditForm

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditDetailProduct(){
    Scaffold(
        topBar = {
            Box {
                Image(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(65.dp),
                    painter = painterResource(id = R.drawable.bgappbar),
                    contentDescription = "Background image"
                    , contentScale = ContentScale.Crop

                )

                TopAppBar(
                    colors = TopAppBarColors(containerColor = Color.Transparent, actionIconContentColor = Color.White, navigationIconContentColor = Color.White, scrolledContainerColor = Color.Transparent, titleContentColor = Color.White) ,
                    modifier = Modifier
                        .background(color = Color.Transparent)
                        .padding(start = 5.dp, end = 5.dp),
                    title = { Text(text = "Edit Detail") },
                    navigationIcon = {
                        Icon(modifier = Modifier.clickable {
//                        navBack()
                        },imageVector = Icons.Default.ArrowBack, contentDescription = "arrow back")
                    },
                    )
            }

        }

    ){
        Column(Modifier.padding(it)){
            Image(painter = painterResource(id = R.drawable.testing_image),modifier = Modifier
                .fillMaxWidth()
                .height(400.dp),contentDescription = "photo image")
            
            Column(Modifier.padding(10.dp)){
                InputEditForm(hint = "insert product name", title = "Product Name")
                InputEditForm(hint = "insert product name", title = "Product Name")
                InputEditForm(hint = "insert product name", title = "Product Name")
                InputEditForm(hint = "insert product name", title = "Product Name")
                InputEditForm(hint = "insert product name", title = "Product Name")
            }

        }

    }
}



@Composable
@Preview(showBackground = true)
fun preview(){
    EditDetailProduct()
}