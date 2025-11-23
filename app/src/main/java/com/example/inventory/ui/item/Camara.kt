package com.example.inventory.ui.item

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import java.io.File

@Composable
fun CameraButton(
    onPhotoTaken: (Uri) -> Unit,
    onVideoTaken: (Uri) -> Unit
) {
    val context = LocalContext.current

    // ------------------------------
    // FOTO (TakePicturePreview)
    // ------------------------------
    val photoLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap: Bitmap? ->
        if (bitmap != null) {
            val file = File(context.filesDir, "IMG_${System.currentTimeMillis()}.jpg")
            file.outputStream().use { out ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
            }
            onPhotoTaken(Uri.fromFile(file))
        }
    }

    // ------------------------------
    // PERMISO DE CÁMARA
    // ------------------------------
    val requestCameraPermission = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            photoLauncher.launch(null)
        } else {
            Toast.makeText(context, "Permiso de cámara denegado", Toast.LENGTH_SHORT).show()
        }
    }

    // ------------------------------
    // VIDEO (CaptureVideo)
    // ------------------------------
    var videoUri by remember { mutableStateOf<Uri?>(null) }

    val videoLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CaptureVideo()
    ) { success ->
        if (success && videoUri != null) {
            onVideoTaken(videoUri!!)
        }
    }

    fun startVideoRecording() {
        val uri = ComposeFileProvider.getImageUri(context)
        videoUri = uri
        videoLauncher.launch(uri)
    }

    // ------------------------------
    // UI
    // ------------------------------
    Column {

        // Foto
        Button(onClick = {
            val permission = ContextCompat.checkSelfPermission(
                context, Manifest.permission.CAMERA
            )
            if (permission == PackageManager.PERMISSION_GRANTED) {
                photoLauncher.launch(null)
            } else {
                requestCameraPermission.launch(Manifest.permission.CAMERA)
            }
        }) {
            Text("Tomar Foto")
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Video
        Button(onClick = { startVideoRecording() }) {
            Text("Tomar Video")
        }
    }
}
