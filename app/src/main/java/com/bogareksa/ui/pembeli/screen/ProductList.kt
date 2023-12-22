package com.bogareksa.ui.pembeli.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bogareksa.ui.pembeli.BogareksaCustomerApp
import com.bogareksa.ui.pembeli.components.Search
import com.bogareksa.ui.pembeli.data.remote.ProductItem
import com.bogareksa.ui.pembeli.viewmodel.ProductListViewModel
import com.bogareksa.ui.penjual.mainSellerComponent.AppbarImgBackgroundNoBack
import com.bogareksa.ui.pembeli.components.ProductItem as ProductItem

@Composable
fun ProductList(
    modifier: Modifier = Modifier,
    navigateToDetail: (String,String,Int,String,String,String) -> Unit,
    viewModel: ProductListViewModel,
){
    Column {
//        Search(
//            viewModel
//        )
        ProductListContent(
            viewModel,
            modifier = modifier,
            navigateToDetail = navigateToDetail
        )
    }
}

@SuppressLint("SuspiciousIndentation")
@Composable
fun ProductListContent(
    viewModel: ProductListViewModel,
    modifier: Modifier = Modifier,
    navigateToDetail: (String,String,Int,String,String,String) -> Unit
){
    val data by viewModel.productList.observeAsState()
    viewModel.getProducts()
    AppbarImgBackgroundNoBack( title = "Product List")
//    Row(
//        modifier = Modifier
//            .fillMaxWidth()
//            .background(Color(0xFF00698C))
//    ){
//        Text(
//            text = "Product List",
//            fontSize = 24.sp,
//            color = Color.White,
//            modifier = Modifier
//                .padding(start = 16.dp, top = 10.dp, bottom = 10.dp)
//        )
//    }
    Spacer(modifier = Modifier.height(12.dp))
    LazyVerticalGrid(
        columns = GridCells.Adaptive(160.dp),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
    ) {
        items(data.orEmpty()) { product ->
            ProductItem(
                data = product,
                navigateToDetail = {
                        name,image,price,desc,seller,date -> navigateToDetail(
                            name,image,price,desc,seller,date
                    )
                },
            )
        }
    }
}

@Preview(showBackground = true, device = Devices.PIXEL_4)
@Composable
fun JetCoffeeAppPreview() {
    MaterialTheme {
        BogareksaCustomerApp()
    }
}