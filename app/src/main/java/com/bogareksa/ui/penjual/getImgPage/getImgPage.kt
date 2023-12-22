package com.bogareksa.ui.penjual.getImgPage

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bogareksa.R
import com.bogareksa.ui.penjual.getImgPage.component.getPermissionDialog
import com.bogareksa.ui.penjual.getImgPage.component.openCamera
import com.bogareksa.ui.penjual.mainSellerComponent.AppbarImgBackground
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun GetImgPage(navBack: () -> Unit){
    val cameraPermissionState: PermissionState = rememberPermissionState(android.Manifest.permission.CAMERA)
    GetImgPageContent(navBack,cameraPermissionState::launchPermissionRequest,cameraPermissionState.status.isGranted)
}



@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun GetImgPageContent(navBack : () -> Unit,onReqPermission : () -> Unit,camPermission : Boolean){
    var imgExists = false

   Scaffold (
       topBar = {
           AppbarImgBackground(navBack = {
                                         navBack()
           }, title = "Upload Image")
       }
   ){



       Column(modifier = Modifier
           .padding(paddingValues = it)
           .padding(top = 5.dp)){

           if(camPermission){
               openCamera()
           }else{
               getPermissionDialog(onReqPermission)
           }
       }
   }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
@Preview(showBackground = true)
fun preview(){
    val cameraPermissionState: PermissionState = rememberPermissionState(android.Manifest.permission.CAMERA)
    GetImgPageContent({ },{},false )
}