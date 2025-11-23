package com.example.inventory

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.inventory.ui.theme.InventoryTheme
import android.Manifest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass

class MainActivity : ComponentActivity() {

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        permissions.entries.forEach { entry ->
            if (!entry.value && entry.key == Manifest.permission.POST_NOTIFICATIONS) {
                Toast.makeText(this, "Permiso de notificaciones denegado.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            InventoryTheme {
                val windowSize = calculateWindowSizeClass(this)
                Surface(modifier = Modifier.fillMaxSize()) {
                    InventoryApp(windowSize)
                }
            }
        }
    }
}
