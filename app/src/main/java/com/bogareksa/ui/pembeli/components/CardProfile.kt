package com.bogareksa.ui.pembeli.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bogareksa.R

@Composable
fun CardUserProfile(
    customerName : String,
    customerEmail:String,
    modifier: Modifier = Modifier
){
    Row(
        modifier
            .height(70.dp)
            .fillMaxWidth()
    ){
        Image(
            modifier = modifier
            .size(70.dp)
            .clip(CircleShape),painter = painterResource(id = R.drawable.profilevector),
            contentDescription = "avatar"
        )
        Spacer(modifier = modifier.width(24.dp))
        Column(
            modifier
                .fillMaxWidth()
                .padding(5.dp)
        ){
            Text(
                text = customerName,
                style = MaterialTheme.typography.titleMedium.copy(fontSize = 16.sp)
            )
            Spacer(modifier = modifier.height(10.dp))
            Text(
                text = customerEmail,
                style = MaterialTheme.typography.labelSmall.copy(
                    fontSize = 15.sp,
                    fontWeight = FontWeight.ExtraLight
                )
            )
        }

    }
}


@Composable
@Preview(showBackground = true)
fun Preview(){
    MaterialTheme {
        CardUserProfile("Customer", "cutomer1@gmail.com")
    }
}