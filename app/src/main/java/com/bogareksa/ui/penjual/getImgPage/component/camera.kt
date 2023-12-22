package com.bogareksa.ui.penjual.getImgPage.component

import android.graphics.Bitmap
import android.util.Log
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.LinearLayout
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bogareksa.core.utils.rotateBitmap


@Composable
fun openCamera(

){
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraControll = remember {
        LifecycleCameraController(context)
    }


    val getViewmodel = viewModel<CamViewModel>()
    val imgBitmap by getViewmodel.imgBitmap.collectAsState()

    var imgTaken by remember {
        mutableStateOf<Bitmap?>(null)
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
//        bottomBar = {
//            Row(
//                verticalAlignment = Alignment.CenterVertically
//            ){
//                if (imgTaken == null){
//                    Box(modifier = Modifier
//                        .width(100.dp)
//                        .height(100.dp)
//                        .background(color = Color.Gray))
//                }else{
//                    PreviewImage(img = imgTaken!!, listImg = imgBitmap)
//                }
//                Spacer(modifier = Modifier.weight(1f))
//                ExtendedFloatingActionButton(
//                    onClick = {
//                        val execute = ContextCompat.getMainExecutor(context)
//                        cameraControll.takePicture(execute,object : ImageCapture.OnImageCapturedCallback(){
//                            override fun onCaptureSuccess(image: ImageProxy) {
//                                val correctedBitmap: Bitmap = image
//                                    .toBitmap()
//                                    .rotateBitmap(image.imageInfo.rotationDegrees)
//
//
//                                imgTaken = correctedBitmap
//                                getViewmodel.getPhoto(correctedBitmap)
//                                image.close()
//                            }
//
//                            override fun onError(exception: ImageCaptureException) {
//                                Log.e("CameraContent", "Error capturing image", exception)
//                            }
//                        })
//                    }) {
//                    Text(text = "Take Image")
//                }
//            }
//
//        },


        ){
        Box {
            AndroidView(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(500.dp)
                    .padding(it),
                factory = {
                    PreviewView(context).apply {
                        layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT)
                        setBackgroundColor(0x000000)
                        scaleType = PreviewView.ScaleType.FILL_START
                    }.also {
                        it.controller = cameraControll
                        cameraControll.bindToLifecycle(lifecycleOwner)
                    }
                })
        }


    }
}

