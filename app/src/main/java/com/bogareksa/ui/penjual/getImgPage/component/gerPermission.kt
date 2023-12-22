package com.bogareksa.ui.penjual.getImgPage.component

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun getPermissionDialog(requestPermission : () -> Unit){
    Button(onClick = {
        requestPermission()
    }) {
        Text(text = "Get Permission")
    }
}