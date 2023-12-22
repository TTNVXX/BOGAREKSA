package com.bogareksa.ui.pembeli.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.bogareksa.R
import com.bogareksa.ui.pembeli.data.remote.ProductItem

@Composable
fun ProductItem(
    data: ProductItem,
    modifier: Modifier = Modifier,
    navigateToDetail: (String,String,Int,String,String,String) -> Unit
){
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .background(Color.White)
            .width(160.dp)
            .border(
                width = 1.dp,
                color = Color(0x26000000),
                shape = RoundedCornerShape(size = 10.dp)
            )
            .clickable {
                navigateToDetail(
                    data.name.toString(),
                    data.imageUrl.toString(),
                    data.price!!.toInt(),
                    data.desc.toString(),
                    data.ownerId.toString(),
                    data.predictionResult?.detectedDate.toString(),
                )
            }
    ){
        AsyncImage(
            model = data.imageUrl,
            contentDescription = "image description",
            contentScale = ContentScale.Crop,
            modifier = modifier
                .height(144.dp)
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
            modifier
                .padding(8.dp)
        ){
            Text(
                text = data.name.toString(),
                color = Color.Black,
                modifier = modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            )
            Text(
                text = data.desc.toString(),
                color = Color.Black,
                maxLines = 1,
                fontSize = 12.sp,
                modifier = modifier
                    .fillMaxWidth()
            )
            Text(
                text = stringResource(R.string.rupiah, data.price.toString()),
                color = Color.Black,
                modifier = modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            )
        }

    }
}
@Composable
@Preview(showBackground = true)
fun ProductItemPreview() {
    MaterialTheme {
        val product = ProductItem(
            productId = "1",
            imageUrl = "R.drawable.food",
            name = "Fast Food",
            desc = "Deskripsi",
            price = 90000
        )
        ProductItem(
            data = product,
            modifier = Modifier
        ){
            name,imageUrl,price,desc,seller,date ->

        }
    }
}
