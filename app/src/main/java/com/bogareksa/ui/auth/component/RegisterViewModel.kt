package com.bogareksa.ui.auth.component

import android.provider.ContactsContract.CommonDataKinds.Email
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bogareksa.io.response.ResponseAuth
import com.bogareksa.io.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Response

class RegisterViewModel : ViewModel(){


    private val _regisAuth = MutableLiveData<ResponseAuth>()
    val regisAuth : LiveData<ResponseAuth> = _regisAuth

    fun getAuthRegister(email: String,password : String, username : String, role : String){
        val client = ApiConfig.getApiService().postRegister(email,password,username,role)
        client.enqueue(object :retrofit2.Callback<ResponseAuth> {
            override fun onResponse(
                call: Call<ResponseAuth>,
                response: Response<ResponseAuth>
            ) {
                if (response.isSuccessful) {
                    _regisAuth.value = response.body()
                } else {
                    Log.e("AuthLoginViewModel", "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ResponseAuth>, t: Throwable) {
                Log.e("AuthLoginViewModel Error", "onFailure: ${t.message.toString()}")
            }

        })
    }


}