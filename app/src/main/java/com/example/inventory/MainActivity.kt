package com.example.inventory

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.inventory.ui.theme.InventoryTheme
import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import android.app.AlarmManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import android.provider.Settings
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import com.example.inventory.Alarma.AlarmItem
import com.example.inventory.Alarma.AlarmSchedulerImpl
import com.example.inventory.ui.item.programarAlarma
import java.time.LocalDateTime

class MainActivity : ComponentActivity() {

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        permissions.entries.forEach { entry ->
            if (!entry.value) {
                if (entry.key == Manifest.permission.POST_NOTIFICATIONS) {
                    Toast.makeText(this, "Permiso de notificaciones denegado.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        setContent {
            InventoryTheme {
                // Detecta que tipo de pantalla es
                val windowSize = calculateWindowSizeClass(this)
                Surface(modifier = Modifier.fillMaxSize()) {

                    InventoryApp(windowSize)
                }
            }
        }
    }




}
