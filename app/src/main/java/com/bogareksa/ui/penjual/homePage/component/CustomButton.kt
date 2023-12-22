package com.bogareksa.ui.penjual.homePage.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun CustomButton(title:String,onClick:()->Unit){
  Button(
      colors = ButtonColors(containerColor = Color(0xff00698C), disabledContentColor = Color(0xff00698C), contentColor = Color.White, disabledContainerColor = Color(0xff00698C)),
      shape = MaterialTheme.shapes.medium,
      onClick = onClick) {
      Text(text = title)
  }

}


@Composable
fun CustomButtonFillWidth(title:String,onClick:()->Unit){
    Button(
        colors = ButtonColors(containerColor = Color(0xff00698C), disabledContentColor = Color(0xff00698C), contentColor = Color.White, disabledContainerColor = Color(0xff00698C)),
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        onClick = onClick) {
        Text(text = title)
    }

}

@Composable
@Preview(showBackground = true)
fun ShowPreview(){
//    CustomButton(title = "See All Product")
}