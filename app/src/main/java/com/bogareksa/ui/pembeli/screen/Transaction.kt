package com.bogareksa.ui.pembeli.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bogareksa.ui.pembeli.components.TransactionItem

@Composable
fun TransactionContent(
    modifier: Modifier = Modifier,
){
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
            .padding(horizontal = 16.dp)
    ){
        repeat(10){
//            item{
//                TransactionItem()
//            }
        }
    }
}
@Composable
@Preview(showBackground = true)
fun TransactionPreview() {
    MaterialTheme {
        TransactionContent()
    }
}