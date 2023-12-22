package com.bogareksa.ui.pembeli.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.bogareksa.R
import com.bogareksa.ui.pembeli.data.local.CartEntity

@Composable
fun CartItem (
    data: CartEntity,
    amountChanged: (id: Int, count: Int) -> Unit,
    modifier: Modifier = Modifier
){
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
    ){
        AsyncImage(
            model = data.imageUrl,
            contentDescription = "image description",
            contentScale = ContentScale.Crop,
            modifier = modifier
                .size(80.dp)
                .fillMaxWidth()
                .clip(
                    shape = RoundedCornerShape(
                        topStart = 10.dp,
                        topEnd = 10.dp,
                        bottomStart = 0.dp,
                        bottomEnd = 0.dp
                    )
                )
        )
        Column(
            modifier = modifier
                .fillMaxWidth()
                .height(75.dp)
                .background(
                    color = Color(0xFF3B8197),
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(8.dp),
        ) {
            Text(
                text = data.name,
                color = Color.White,
                fontSize = 20.sp,
                maxLines = 1,
                modifier = modifier
                    .fillMaxWidth()
            )
            Row(
                verticalAlignment = Alignment.Bottom,
                modifier = modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
            ){
                Text(
                    text = stringResource(R.string.rupiah, data.price),
                    color = Color.White,
                    fontSize = 20.sp,
                )
                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}
//@Composable
//@Preview(showBackground = true)
//fun CartItemPreview() {
//    MaterialTheme {
//        val product = CartEntity(
//            imageUrl = "R.drawable.food",
//            name = "Fast Food",
//            price = 90000,
//            amount = 1
//        )
//        CartItem(
//            data = product,
//            count = 1,
//            amountChanged = {productId,count ->}
//        )
//    }
//}
