package com.bogareksa.ui.penjual.detailProductFromList

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.OnBackPressedDispatcher
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.bogareksa.R
import com.bogareksa.io.response.MyProductsItem
import com.bogareksa.ui.penjual.detailProductPage.component.AppbarDetailImgBackground
import com.bogareksa.ui.penjual.detailProductPage.component.DetaiProductSellerlViewModel
import com.bogareksa.ui.penjual.listProductPage.component.ProductSellerViewModel

class DetailFromListActivity : AppCompatActivity() {

    private lateinit var viewmodelDetail :DetaiProductSellerlViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewmodelDetail = ViewModelProvider(this,ViewModelProvider.NewInstanceFactory()).get(modelClass = DetaiProductSellerlViewModel::class.java)


        val myProductItem: MyProductsItem? = intent.getSerializableExtra("data") as? MyProductsItem
        if(myProductItem != null){
            setContent {

                detailActivityContent(
                    onBack = {onBackPressed()}
                    ,product = myProductItem, vmDetail = viewmodelDetail)
            }
        }else{
            setContent{
                CircularProgressIndicator()
            }
        }

    }
}

            @Composable
            fun detailActivityContent(onBack:() -> Unit,product:MyProductsItem,modifier: Modifier = Modifier,vmDetail:DetaiProductSellerlViewModel){
                Scaffold(
                    topBar = {
                        AppbarDetailImgBackground(
                            navBack = {
                                //run popBackStage
                                      onBack()
                            }, title = "Detail Product", id = product.productId.toString(), token = "", vmDetail = vmDetail)

                    }
                ){
                    Column(
                        modifier= Modifier
                            .padding(paddingValues = it)
                            .verticalScroll(rememberScrollState())){


                    Image(painter = rememberAsyncImagePainter(product.imageUrl), contentDescription ="title", modifier = Modifier
                        .height(400.dp)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(bottomStart = 40.dp, bottomEnd = 40.dp)) )


            Column(
                modifier.padding(15.dp)
            ){
                Text(text = "this is product name" , style = MaterialTheme.typography.titleLarge)
                Row {
                    Text(text = "terjual")
                    Spacer(modifier = modifier.width(15.dp))
                    Row {
                        Icon(imageVector = Icons.Default.Star, contentDescription = "star", tint = Color.Yellow)
                        Text(text = "5")
                    }
                }
                Spacer(modifier = modifier.height(20.dp))
                Text(text = "Rp1000",style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.ExtraBold
                ))
                Spacer(modifier = modifier.height(20.dp))
                Divider()
                Spacer(modifier = modifier.height(10.dp))
                Text(text = "Detail", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                Spacer(modifier = modifier.height(5.dp))
                Text(text = "detail")
                Spacer(modifier = modifier.height(20.dp))


                Divider()
                Spacer(modifier = modifier.height(10.dp))


                Spacer(modifier = modifier.height(10.dp))
//                if (true){
//                    Button(onClick = {
//                    },modifier.fillMaxWidth()) {
//                        Text(text = "Add To Cart")
//                    }
//                }else{
//                    Button(onClick = {
//
//                    },modifier.fillMaxWidth(), colors = ButtonColors(containerColor = Color.Gray, contentColor = Color.Black, disabledContainerColor = Color.White, disabledContentColor = Color.Gray)) {
//                        Text(text = "Add To Cart")
//                    }
//                }

            }

        }
    }
}
