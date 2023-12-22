package com.bogareksa.ui.penjual.homePage

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.bogareksa.R
import com.bogareksa.sessions.LoginSession
import com.bogareksa.ui.auth.component.LoginViewModel
import com.bogareksa.ui.navigation.Screen
import com.bogareksa.ui.pembeli.viewmodel.CartViewModel
import com.bogareksa.ui.penjual.homePage.component.BoxData
import com.bogareksa.ui.penjual.homePage.component.CardItem
import com.bogareksa.ui.penjual.homePage.component.CardProfile
import com.bogareksa.ui.penjual.homePage.component.CustomButton
import com.bogareksa.ui.penjual.homePage.component.CustomButtonFillWidth
import com.bogareksa.ui.penjual.listProductPage.component.ProductSellerViewModel
import com.bogareksa.ui.penjual.mainSellerComponent.VerticalSpace
import java.io.File


@Composable
fun HomePageSeller(
    orderVm : CartViewModel,
    navCrontroller: NavHostController,
    email:String,vm: ProductSellerViewModel,
    toTheListProduct: () -> Unit,
    getAddPageRoute : () -> Unit,
    toTheNotification: () -> Unit
//    toTheDetail : () -> Unit
){

    val session = LoginSession(ctx = LocalContext.current)
    var user: HashMap<String,String> = session.getUserProduct()
    val castToTxt = user.toString()
    val theToken = castToTxt.substringAfter("{token=").substringBefore("}")

    Box(modifier = Modifier){
        HomePageContent(

            navControl = navCrontroller,
            email= email,
            token = theToken,
            vm = vm,
            getAddPageRoute = getAddPageRoute,
            toTheListProduct = toTheListProduct,
            toTheNotification = toTheNotification,
            orderVm = orderVm
//            toDetailPage = toTheDetail
        )
    }
}



@SuppressLint("SuspiciousIndentation")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePageContent(
    orderVm: CartViewModel,
    navControl:NavHostController,
    email:String, token: String,
    vm: ProductSellerViewModel,
    modifier: Modifier = Modifier,
    toTheListProduct: () -> Unit,
//    toDetailPage : () -> Unit,
    toTheNotification: () -> Unit,
    getAddPageRoute : () -> Unit){



    val listProuctData by  vm.listProducts.observeAsState()
    val isFetch by rememberUpdatedState(newValue = vm.isFetch.observeAsState())
//    val theData = listProuctData.value ?: emptyList()

    val scrollState = rememberScrollState()


            vm.findProducts(token)


    Scaffold(
       topBar = {
           TopAppBar(
               actions = {
                   Image(imageVector = Icons.Default.Settings,modifier = modifier.size(25.dp).clickable {
                       toTheNotification()
                   },contentDescription = "notification")
               },
               navigationIcon = {
                  
               },
               title = {
               Text(text = "Hello Seller")

           },)
       }
    ){
        Column(
            modifier
                .fillMaxWidth()
                .padding(paddingValues = it)
                .padding(15.dp)
                .verticalScroll(state = scrollState)
                .height(820.dp)
                ){
            CardProfile(sellerName = email, sellerEmail = email)
VerticalSpace()
            HorizontalDivider()
            VerticalSpace()

            LazyVerticalGrid(


                columns = GridCells.Adaptive(140.dp),
              contentPadding = PaddingValues(bottom = 15.dp),
                horizontalArrangement = Arrangement.spacedBy(20.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ){
                item {
                    BoxData(title = "Products Amount",
                        amount = "10"
//                        amount = if(theData.isEmpty()) "-" else theData.size.toString()
                    )
                }
                item {
                    BoxData(title = "Proudcts Exp", amount = "3")
                }
            }
            VerticalSpace()


            Row(modifier = modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically){
                Text(text = stringResource(id = R.string.product_title), style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.weight(1f))
//                Button(onClick =
//                    getAddPageRoute
//                ) {
//                    Text(text = "Add")
//                }
                CustomButton(title = "Add Product", onClick = getAddPageRoute)
//                Box(
//                    modifier = Modifier
//                        .height(35.dp)
//                        .clip(
//                            RoundedCornerShape(10.dp)
//                        )
//                        .background(color = Color(0xff00698C))
//                        .clickable {
//
//                        }
//                        .padding(bottom = 10.dp)
//                ){
//                    Text(text = "Add Product", modifier = Modifier.padding(10.dp).align(Alignment.Center), style = MaterialTheme.typography.bodyLarge.copy(color = Color.White))
//                }
            }

            VerticalSpace()

            if(listProuctData == null){
                CircularProgressIndicator()
            }else{
                LazyColumn{
                    items(listProuctData!!.take(5)){productItem ->
                        CardItem(data = productItem,toDetail = navControl)
                        HorizontalDivider()
                    }
                }
            }



            VerticalSpace()
            CustomButtonFillWidth(title = "See All Products", onClick = toTheListProduct)
        }
    }
}



@Composable
@Preview(showBackground = true)
fun preview(){

}