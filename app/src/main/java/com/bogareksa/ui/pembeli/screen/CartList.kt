package com.bogareksa.ui.pembeli.screen

import android.app.Application
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bogareksa.ui.pembeli.CustomerRepository
import com.bogareksa.ui.pembeli.components.CartItem
import com.bogareksa.ui.pembeli.components.TransactionItem
import com.bogareksa.ui.pembeli.viewmodel.CartViewModel
import com.bogareksa.ui.pembeli.viewmodel.ProductDetailViewModel
import com.bogareksa.ui.penjual.mainSellerComponent.AppbarImgBackgroundNoBack

@Composable
fun CartList(
    viewModel: CartViewModel,
){
    CartListContent(
        viewModel,
        onProductAmountChanged = { productid, count -> },
        )
}
@Composable
fun CartListContent(
    viewModel: CartViewModel,
    onProductAmountChanged: (id: Int, count: Int) -> Unit,
    modifier: Modifier = Modifier
){
    val data by viewModel.cartList.observeAsState()
    viewModel.getCartList()
    Surface(
        modifier = modifier.fillMaxSize()
    ) {
        Column{
            AppbarImgBackgroundNoBack( title = "Order List")
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .background(Color(0xFF00698C))
//            ){
//                Text(
//                    text = "Order List",
//                    fontSize = 24.sp,
//                    color = Color.White,
//                    modifier = Modifier
//                        .padding(start = 16.dp, top = 10.dp, bottom = 10.dp)
//                )
//            }
            Spacer(modifier = Modifier.height(24.dp))
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp)
            ) {
                itemsIndexed(data.orEmpty()) {idx,cart ->
                    TransactionItem(
//                        vm= viewModel,
                        data = cart,
                        index = idx)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Preview(showBackground = true, device = Devices.PIXEL_4)
@Composable
fun CartListPreview() {
    val viewModel = CartViewModel(CustomerRepository.getInstance(Application()))
    MaterialTheme {
        CartList(viewModel = viewModel)
    }
}