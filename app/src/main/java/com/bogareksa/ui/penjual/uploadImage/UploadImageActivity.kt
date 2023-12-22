package com.bogareksa.ui.penjual.uploadImage

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.lifecycle.ViewModelProvider
import com.bogareksa.databinding.ActivityUploadImageBinding
import com.bogareksa.ui.penjual.addProductPage.AddProductActivity
import com.bogareksa.ui.penjual.addProductPage.component.AddProductViewModel
import com.bogareksa.ui.penjual.uploadImage.component.CameraActivity
import com.bogareksa.ui.penjual.uploadImage.component.CameraActivity.Companion.CAMERAX_RESULT

class UploadImageActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUploadImageBinding


    private var currentImageUri: Uri? = null

    private var cameraSelector: CameraSelector = CameraSelector.DEFAULT_BACK_CAMERA


    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(this, "Permission request granted", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Permission request denied", Toast.LENGTH_LONG).show()
            }
        }

    private fun allPermissionsGranted() =
        ContextCompat.checkSelfPermission(
            this,
            REQUIRED_PERMISSION
        ) == PackageManager.PERMISSION_GRANTED


    companion object {
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
    }

    private lateinit var viewModel : AddProductViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this,
            ViewModelProvider.NewInstanceFactory())[AddProductViewModel::class.java]

        binding = ActivityUploadImageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.elevation = 0f

        if(!allPermissionsGranted()){
            requestPermissionLauncher.launch(REQUIRED_PERMISSION)
        }


        binding.galleryCam.setOnClickListener {
            startGallery()
        }

        binding.btnCam.setOnClickListener {
            startCamera()
        }

        binding.backBtnUpload.setOnClickListener{
            onBackPressed()
        }


        binding.btnScan.setOnClickListener {
            if(currentImageUri != null){
                Log.d("uir img from upload", currentImageUri.toString())
                val itn = Intent(this,AddProductActivity::class.java)
                itn.putExtra("img",currentImageUri.toString())
                itn.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(itn)
            }
        }
    }


    private fun startCamera() {
        val itn = Intent(this,CameraActivity::class.java)
        launcherIntentCameraX.launch(itn)
    }


    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            showImage()
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }


    private fun showImage() {
        currentImageUri?.let {uri ->
            Log.d("Image URI", "showImage: $uri")
            binding.prevImage.setImageURI(uri)

        }
    }



    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERAX_RESULT) {
            currentImageUri = it.data?.getStringExtra(CameraActivity.EXTRA_CAMERAX_IMAGE)?.toUri()
            showImage()
        }
    }

}