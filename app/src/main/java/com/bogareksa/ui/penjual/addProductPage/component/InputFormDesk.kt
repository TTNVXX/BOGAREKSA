package com.bogareksa.ui.penjual.addProductPage.component
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
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
fun InputAddFormDesk(txt: String,hint:String,title:String,onChage: (String) -> Unit){
//    var txt by remember{ txt}
    var valid by remember{ mutableStateOf(true) }


    Column {
        Text(text = title, style = MaterialTheme.typography.bodyLarge)
        VerticalSpace()
        Box(modifier = Modifier
            .background(color = Color.White)
            .clip(RoundedCornerShape(10.dp))
            .border(width = 3.dp, color = Color(0xff00698C))


        ){
            TextField(
                minLines = 3,
                modifier = Modifier.fillMaxWidth()
                ,value = txt,
                onValueChange ={
                    onChage(it)
                    valid = it.isNotEmpty()
//                    txt.value = it
//                    valid = it.isNotEmpty()
                } ,
                label = { Text(text = hint)},
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
@Preview(showBackground = true)
fun previewDesk(){
//    InputAddForm(hint = "insert text", title = "test", onChage = "")
}