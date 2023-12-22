package com.bogareksa.ui.penjual.addProductPage.component

import android.annotation.SuppressLint
import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import androidx.core.net.toFile
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.loader.content.CursorLoader
import com.bogareksa.io.response.ResponseAddProduct
import com.bogareksa.io.response.ResponseDeleteProduct
import com.bogareksa.io.retrofit.ApiConfig
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class AddProductViewModel : ViewModel(){

    private val _upResponse =  MutableLiveData<ResponseAddProduct>()
    val upResponse : LiveData<ResponseAddProduct> get() = _upResponse


    private var _isLogin = MutableLiveData<Boolean>(false)
    var isLogin : LiveData<Boolean> = _isLogin

    private var _showDialog = MutableLiveData<Boolean>(false)
    var showDialog : LiveData<Boolean> = _showDialog

    var hasImage : Boolean = false


    fun dialogShow(isShow : Boolean){
        _showDialog.value = isShow
//        if(msg == "Detected date is valid"){
//            dataValid = true
//            _showDialog.value = true
////            Log.d("result from api post valid",resultData.value?.data?.message.toString())
//        }else if(msg != "Detected date is valid" && msg != null){
//            dataValid = false
//            _showDialog.value = true
////            Log.d("result from api post not valid",resultData.value?.data?.message.toString())
//        }else{
//            Log.d("result from api post",msg.toString())
//        }

    }

    fun getRealPathFromURI(context: Context, contentUri: Uri): String? {
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        var result: String? = null

        val cursorLoader = CursorLoader(context, contentUri, proj, null, null, null)
        val cursor = cursorLoader.loadInBackground()

        if (cursor != null) {
            val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor.moveToFirst()
            result = cursor.getString(columnIndex)
            cursor.close()
        }
        return result
    }



    fun uploadProduct(token:String,name:String,price:Int,uploaded:File){
        _isLogin.value = true



        val builder = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("price", price.toString())
            .addFormDataPart("name", name)
            .addPart(MultipartBody.Part.createFormData("uploadedFile", uploaded.name, RequestBody.create(
                "multipart/form-data".toMediaTypeOrNull(), uploaded.name)))

        val requestBody = builder.build()

//
//        val filePart = MultipartBody.Part.createFormData(
//            "uploadedFile",
//            uploaded.name,
//            RequestBody.create("image/png".toMediaTypeOrNull(), uploaded)
//        )

//        val client = ApiConfig.getApiService().addProduct(token,filePart,price,name)
        val client = ApiConfig.getApiService().addProduct(token,requestBody)
        client.enqueue(object : Callback<ResponseAddProduct> {

            override fun onResponse(
                call: Call<ResponseAddProduct>,
                response: Response<ResponseAddProduct>
            ) {
                if (response.isSuccessful) {
                    _upResponse.value = response.body()
                    Log.d("berhasil add product product","success add new product cuy ${_upResponse.value?.data?.message}")
                    _isLogin.value =  false
                    _showDialog.value = true
                } else {
                    Log.e(ContentValues.TAG, "onFailure: ${response.message()}")
                    _isLogin.value =  false

                }
            }

            override fun onFailure(call: Call<ResponseAddProduct>, t: Throwable) {
                Log.e(ContentValues.TAG, "onFailure: ${t.message.toString()}")
                _isLogin.value =  false
            }
        })
    }















}