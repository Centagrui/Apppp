package com.example.inventory.ui.item

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.media.MediaRecorder
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import java.io.File

@Composable
fun AudioRecorderButton(
    onAudioSaved: (Uri) -> Unit
) {
    val context = LocalContext.current
    var isRecording by remember { mutableStateOf(false) }
    val recorder = remember { MediaRecorder() }
    var outputFile by remember { mutableStateOf<File?>(null) }

    // ------------------------------
    // Permiso de micrófono
    // ------------------------------
    val requestPermission = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (!granted) {
            Toast.makeText(context, "Permiso de micrófono denegado", Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(Unit) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO)
            != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermission.launch(Manifest.permission.RECORD_AUDIO)
        }
    }

    // ------------------------------
    // Iniciar grabación
    // ------------------------------
    fun startRecording() {
        val file = File(context.filesDir, "AUD_${System.currentTimeMillis()}.mp3")
        outputFile = file

        recorder.apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            setOutputFile(file.absolutePath)
            prepare()
            start()
        }

        isRecording = true
    }

    // ------------------------------
    // Detener grabación
    // ------------------------------
    fun stopRecording() {
        recorder.apply {
            stop()
            reset()
        }

        isRecording = false

        outputFile?.let { file ->
            onAudioSaved(Uri.fromFile(file))
        }
    }

    Button(onClick = {
        if (isRecording) stopRecording()
        else startRecording()
    }) {
        Text(if (isRecording) "Detener" else "Audio")
    }
}
