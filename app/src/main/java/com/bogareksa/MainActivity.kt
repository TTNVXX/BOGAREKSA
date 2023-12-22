package com.bogareksa

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.bogareksa.sessions.LoginSession
import com.bogareksa.ui.penjual.SellerMainPage
import com.bogareksa.ui.penjual.homePage.HomePageSeller

class MainActivity : AppCompatActivity() {


    lateinit var session : LoginSession
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val email = intent.getStringExtra("email")
        setContent {

//            session = LoginSession(this)
//
//            var user: HashMap<String,String> = session.getUserProduct()
//
//
//            Log.d("session string" , user.toString())



                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SellerMainPage(email = email.toString())
                }
            }

    }
}