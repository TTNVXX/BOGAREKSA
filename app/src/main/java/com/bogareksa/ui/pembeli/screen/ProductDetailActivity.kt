package com.bogareksa.ui.pembeli.screen

import android.annotation.SuppressLint
import android.app.Application
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.bogareksa.R
import com.bogareksa.ui.pembeli.CustomerRepository
import com.bogareksa.ui.pembeli.components.BuyButton
import com.bogareksa.ui.pembeli.components.ProductCounter
import com.bogareksa.ui.pembeli.data.remote.ProductItem
import com.bogareksa.ui.pembeli.viewmodel.ProductDetailViewModel
import com.bogareksa.ui.penjual.mainSellerComponent.AppbarImgBackground
import com.bogareksa.ui.penjual.mainSellerComponent.AppbarImgBackgroundNoBack

class ProductDetailActivity : AppCompatActivity() {

    private lateinit var repository: CustomerRepository
    private lateinit var viewModel: ProductDetailViewModel

    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)

        repository = CustomerRepository.getInstance(application)
        viewModel = ProductDetailViewModel(repository)

        val name: String = intent.getStringExtra("name").toString()
        val price: Int = intent.getIntExtra("price",0)
        val image: String = intent.getStringExtra("image").toString()
        val desc: String = intent.getStringExtra("desc").toString()
//        val seller: String = intent.getStringExtra("seller").toString()
        val date: String = intent.getStringExtra("date").toString()
            setContent {
                DetailActivity(
                    name = name,
                    price = price,
                    image = image,
                    desc = desc,
//                    seller = seller,
                    date = date,
                    viewModel = viewModel,
                    product = ProductItem(),
                    count = 1,
                    back = {
                        onBackPressed()
                    }
                )
            }
        }
    }

@Composable
fun DetailActivity(
    back: () -> Unit,
    image: String,
    name: String,
    price: Int,
    desc: String,
    count: Int,
//    seller: String,
    date: String,
    viewModel: ProductDetailViewModel,
    product: ProductItem,
    modifier: Modifier = Modifier,
){
    var totalPrice by rememberSaveable { mutableStateOf(0) }
    var orderCount by rememberSaveable { mutableStateOf(count) }

    Column(modifier = modifier) {
        AppbarImgBackground(navBack = back, title = "Detail Product")
        Column(
            modifier
                .verticalScroll(rememberScrollState())
                .weight(1f)
                .fillMaxWidth()
        ) {
            Box(
                modifier
                    .padding(bottom = 12.dp, top = 10.dp),
            ){
                Image(
                    painter = rememberAsyncImagePainter(image),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = modifier
                        .fillMaxWidth()
                        .height(360.dp)
                )
            }
            Column(
                modifier
                    .padding(start = 12.dp, end = 12.dp, bottom = 24.dp),
            ) {
                Text(
                    text = name,
                    color = Color.Black,
                    fontSize = 22.sp,
                    modifier = modifier
                        .fillMaxWidth()
                )
                Text(
                    text = stringResource(R.string.rupiah, price.toString()),
                    color = Color.Black,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.ExtraBold,
                    modifier = Modifier.padding(top = 24.dp)
                )
                HorizontalDivider()
                Text(
                    text = stringResource(R.string.exp, date),
                    color = Color.Black,
//                    fontSize = 24.sp,
//                    fontWeight = FontWeight.ExtraBold,
                    modifier = Modifier.padding(vertical = 16.dp)
                )
//                Text(
//                    text = stringResource(R.string.toko, seller),
//                    color = Color.Black,
//                    fontSize = 24.sp,
//                    modifier = modifier
//                        .padding(bottom = 12.dp)
//                )
                Text(
                    text = "Detail produk",
                    color = Color.Black,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = desc,
                    color = Color.Black,
                    fontSize = 24.sp,
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp)
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFF00698C))
            ) {
                totalPrice = price * orderCount
                Text(
                    text = stringResource(R.string.rupiah, totalPrice),
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.ExtraBold,
                    modifier = Modifier
                        .padding(8.dp)
                )
                Spacer(modifier = Modifier.weight(1f))
                ProductCounter(
                    1,
                    orderCount,
                    increased = {orderCount++},
                    decreased = {if (orderCount > 0) orderCount--},
                    modifier = Modifier
                        .padding(end = 8.dp)
                )
            }
            BuyButton(
                text = "buy",
                enabled = orderCount > 0,
                modifier = modifier.padding(12.dp),
                onClick = {
                    viewModel.addToCart(
                        name = name,
                        imageUrl = image,
                        amount = orderCount,
                        totalPrice = totalPrice
                    )
                    back()
                }
            )
        }
    }
}

