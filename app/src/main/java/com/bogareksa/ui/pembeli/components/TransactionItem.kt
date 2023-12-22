package com.bogareksa.ui.pembeli.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.bogareksa.R
import com.bogareksa.ui.pembeli.data.local.CartEntity
import com.bogareksa.ui.pembeli.viewmodel.CartViewModel

@Composable
fun TransactionItem (
//    vm : CartViewModel,
    data: CartEntity,
    index : Int
){
    Column(
        modifier = Modifier
            .border(
                width = 1.dp,
                color = Color(0x26000000),
                shape = RoundedCornerShape(size = 10.dp)
            )
            .padding(12.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
        ){
            AsyncImage(
                model = data.imageUrl,
                contentDescription = "image description",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(75.dp)
            )
            Column(
                modifier = Modifier
                    .height(75.dp)
                    .padding(start = 8.dp)
            ) {
                Text(
                    text = data.name,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                )
                Text(
                    text = stringResource(R.string.barang, data.amount),
                    fontSize = 16.sp,
                )
            }
//            Spacer(modifier = Modifier.weight(1f))
//            IconButton(onClick = {
//                vm.deleteCart(index)
//            }) {
//                Icon(tint = Color.Red,imageVector = Icons.Default.Delete,contentDescription ="Delete")
//            }
        }
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = "Total Belanja",
            fontSize = 16.sp,
        )
        Text(
            text = stringResource(R.string.rupiah, data.price),
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Lokasi pengambilan barang :",
            modifier = Modifier
        )
        Text(
            text = "Jl. Raya Makam No.5 RT 01 / RW 05 Kecamatan Rembang, Kabupaten Purbalingga , Jawa Tengah Kodepos 53356",
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp)
        )
    }
}
//@Composable
//@Preview(showBackground = true)
//fun TransactionItemPreview() {
//    MaterialTheme {
//        TransactionItem()
//    }
//}