package com.bogareksa.ui.penjual.mainSellerComponent

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.bogareksa.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppbarImgBackground(navBack : () -> Unit,title: String){
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
            title = { Text(text = title) },
            navigationIcon = {
                Icon(modifier = Modifier.clickable {
                        navBack()
                },imageVector = Icons.Default.ArrowBack, contentDescription = title
                )
            },

        )
    }
}