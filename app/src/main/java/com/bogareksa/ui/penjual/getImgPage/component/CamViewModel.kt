package com.bogareksa.ui.penjual.getImgPage.component

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class CamViewModel : ViewModel() {

    private val _imgBitmaps = MutableStateFlow<List<Bitmap>>(emptyList())
    val imgBitmap = _imgBitmaps.asStateFlow()

    fun getPhoto(takenPhoto : Bitmap){
        _imgBitmaps.value += takenPhoto
        if(takenPhoto != null){
            Log.d("image taken",takenPhoto.toString())
        }else{
            Log.d("image taken","no image taken")
        }
    }
}