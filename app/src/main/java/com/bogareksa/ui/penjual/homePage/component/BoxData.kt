package com.bogareksa.ui.penjual.homePage.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.bogareksa.ui.penjual.mainSellerComponent.VerticalSpace

@Composable
fun BoxData(title:String,amount:String,modifier : Modifier = Modifier){
    Box(modifier = modifier
        .width(90.dp)
        .height(90.dp)
        .shadow(elevation = 10.dp,shape = RoundedCornerShape(8.dp))
        .background(color = Color.White, shape = RoundedCornerShape(10.dp))
        .padding(10.dp)

    ){
        Column {
            Text(text = title, style = MaterialTheme.typography.titleSmall)
            HorizontalDivider()
            VerticalSpace()
            Text(text = amount, style = MaterialTheme.typography.titleLarge.copy(color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold))
        }
    }
}