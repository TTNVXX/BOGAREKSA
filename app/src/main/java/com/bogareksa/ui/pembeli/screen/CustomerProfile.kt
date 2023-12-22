package com.bogareksa.ui.pembeli.screen

import android.app.Activity
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bogareksa.R
import com.bogareksa.ui.auth.LoginActivity
import com.bogareksa.ui.pembeli.components.BuyButton
import com.bogareksa.ui.pembeli.components.CardUserProfile
import com.bogareksa.ui.penjual.mainSellerComponent.AppbarImgBackground
import com.bogareksa.ui.penjual.mainSellerComponent.AppbarImgBackgroundNoBack

@Composable
fun CustomerProfile(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
){
    val activityResultLauncher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        // Handle the result if needed
        if (result.resultCode == Activity.RESULT_OK) {
            // Handle success
        } else {
            // Handle failure or canceled
        }
    }
    val ctx = LocalContext.current
    Surface(
        modifier = modifier
            .fillMaxSize()
    ) {
        Column{
            AppbarImgBackgroundNoBack( title = "Profile")
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .background(Color(0xFF00698C))
//            ){
//                Text(
//                    text = "Profile",
//                    fontSize = 24.sp,
//                    color = Color.White,
//                    modifier = Modifier
//                        .padding(start = 16.dp, top = 10.dp, bottom = 10.dp)
//                )
//            }
            Column(
                modifier = modifier
                    .padding(horizontal = 16.dp)
            ){
                Spacer(modifier = Modifier.height(32.dp))
                CardUserProfile("Customer", "cutomer1@gmail.com")
                Spacer(modifier = Modifier.height(24.dp))
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(2.dp)
                        .background(color = Color(0xFF00698C))
                )
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "Settings",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                )
                Spacer(modifier = Modifier.height(64.dp))
                BuyButton(
                    onClick = {
                        activityResultLauncher.launch(
                            Intent(ctx, LoginActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        )
                    },
                    text = "Log Out",
                    modifier = modifier
                        .padding(horizontal = 12.dp)
                )
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun Preview(){
    MaterialTheme {
        CustomerProfile(onBackClick = {})
    }
}