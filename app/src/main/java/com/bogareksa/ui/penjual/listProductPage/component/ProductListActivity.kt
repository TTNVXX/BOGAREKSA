package com.bogareksa.ui.penjual.listProductPage.component

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bogareksa.R
import com.bogareksa.io.response.MyProductsItem
import com.bogareksa.sessions.LoginSession
import com.bogareksa.ui.auth.component.LoginViewModel

class ProductListActivity : AppCompatActivity() {


    private lateinit var viewModel: ProductSellerViewModel
    private lateinit var viewauth : LoginViewModel
    lateinit var session : LoginSession



    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_product_list)
        viewModel = ViewModelProvider(this,ViewModelProvider.NewInstanceFactory()).get(ProductSellerViewModel::class.java)
        viewauth = ViewModelProvider(this,ViewModelProvider.NewInstanceFactory())[LoginViewModel::class.java]

//create the session and get the product token
        session = LoginSession(this)
        var user: HashMap<String,String> = session.getUserProduct()

        runOnUiThread{
            viewModel.listProducts.observe(this, Observer {

                if(it.isEmpty()){
                    val token = user.toString()

                    val result = token.substringAfter("{token=").substringBefore("}")

                    viewModel.findProducts(result)
                }


                setContent {
                    ListPageWithAppbar(navBack = { /*TODO*/ }, products = it )
                }

            })
        }

        setContent {
                Box(modifier = Modifier.fillMaxSize()){
                    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally){
                        CircularProgressIndicator()
                    }
                }
        }
    }
}



@Composable
fun ProductList(products: List<MyProductsItem>) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(140.dp),
        contentPadding = PaddingValues(15.dp),
        horizontalArrangement = Arrangement.spacedBy(20.dp),
        verticalArrangement = Arrangement.spacedBy(17.dp)
    ){
        items(products){
                productData ->
            Log.d("data from listsellerproduct","the data list is not null")
//            ItemCard(image = productData.imageUrl.toString(), title = productData.name.toString(), price = productData.price!!.toInt(), rate = 5)
        }
    }

}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListPageWithAppbar(navBack : () -> Unit,products: List<MyProductsItem>,){
    Scaffold(

        topBar = {
            Box(modifier = Modifier){
                Image(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(75.dp),
                    painter = painterResource(id = R.drawable.bgappbar),
                    contentDescription = "Background image"
                    , contentScale = ContentScale.Crop

                )

                TopAppBar(
                    colors = TopAppBarColors(containerColor = Color.Transparent, actionIconContentColor = Color.White, navigationIconContentColor = Color.White, scrolledContainerColor = Color.Transparent, titleContentColor = Color.White) ,
                    modifier = Modifier
                        .background(color = Color.Transparent)
                        .padding(start = 5.dp, end = 10.dp),
                    title = {
                        SearchItemSeller(deleteText = { /*TODO*/ }, query = "", onQueryChange = {})
                    },
                    navigationIcon = {
                        Icon(modifier = Modifier.clickable {
                            navBack()
                        },imageVector = Icons.Default.ArrowBack, contentDescription = "back page"
                        )
                    },
                    actions = {
                        Icon(imageVector = Icons.Default.Clear, contentDescription ="delete" )
                    }


                )
            }
        }

    ){
        Surface(modifier = Modifier.padding(it)){
            ProductList(products = products )
        }
    }
}


