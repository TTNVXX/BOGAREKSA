package com.bogareksa.ui.auth.component

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bogareksa.io.response.ResponseAuth
import com.bogareksa.io.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback

class LoginViewModel : ViewModel() {
    private val _authData = MutableLiveData<ResponseAuth>()
    val authData : LiveData<ResponseAuth> get() = _authData

    private val _isLogin = MutableLiveData<Boolean>()
    val islogin :LiveData<Boolean> = _isLogin

    fun getAuthLogin(email : String,pass : String){
        _isLogin.value = true
        val client =ApiConfig.getApiService().postLogin(email,pass)
        client.enqueue(object :retrofit2.Callback<ResponseAuth> {
            override fun onResponse(
                call: Call<ResponseAuth>,
                response: Response<ResponseAuth>
            ) {
                if (response.isSuccessful) {
                    _authData.value = response.body()
                    _isLogin.value = false
                } else {
                    Log.e("AuthLoginViewModel", "onFailure: ${response.message()}")
                    _isLogin.value = false
                }
            }

            override fun onFailure(call: Call<ResponseAuth>, t: Throwable) {
                Log.e("AuthLoginViewModel Error", "onFailure: ${t.message.toString()}")
                _isLogin.value = false
            }

        })
    }





}