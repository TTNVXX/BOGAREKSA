package com.bogareksa.ui.penjual.editDetailProductSellerPage.component

import android.provider.CalendarContract.Colors
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bogareksa.ui.penjual.mainSellerComponent.VerticalSpace

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputEditForm(hint:String,title:String,modifier: Modifier = Modifier.padding(top = 10.dp)){
    var txt by remember{ mutableStateOf("") }
    var valid by remember{ mutableStateOf(true) }


    Column {
        Text(text = title, style = MaterialTheme.typography.bodyLarge)
        Spacer(Modifier.height(5.dp))
        Box(modifier = Modifier.background(color = Color.White).clip(RoundedCornerShape(10.dp)).border(width = 3.dp, color = Color(0xff00698C))){
            TextField(
                modifier = Modifier.fillMaxWidth()
                ,value = txt,
                onValueChange ={
                    txt = it
                    valid = it.isNotEmpty()
                } ,
                label = { Text(text = "enter text")},
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.White
                )

                )

            if(!valid){
                Text(text = "please enter a text")
            }
        }

    }
}

@Composable
@Preview
fun preview(){
    InputEditForm(hint = "insert text", title = "test")
}