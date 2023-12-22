package com.bogareksa.ui.pembeli.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bogareksa.R

@Composable
fun AddressItem (
    modifier: Modifier = Modifier
){
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = Color(0xFF6C979C),
                shape = RoundedCornerShape(8.dp)
            )
            .padding(8.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "Alamat",
                color = Color.White,
                modifier = modifier
            )
            Spacer(modifier = Modifier.weight(1f))
            Image(
                painter = painterResource(id = R.drawable.cancel),
                contentDescription = "close",
                contentScale = ContentScale.FillHeight,
                modifier = modifier
                    .height(24.dp)
                    .clickable {}
            )
        }
        Text(
            text = "Jl. Raya Makam No.5 RT 01 / RW 05 Kecamatan Rembang, Kabupaten Purbalingga , Jawa Tengah Kodepos 53356",
            color = Color.White,
            modifier = modifier
                .fillMaxWidth()
                .padding(top = 4.dp)
        )
    }
}
@Composable
@Preview(showBackground = true)
fun AddressItemPreview() {
    MaterialTheme {
        AddressItem()
    }
}