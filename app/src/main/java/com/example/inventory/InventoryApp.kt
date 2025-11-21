package com.example.inventory

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import com.example.inventory.ui.navigation.InventoryNavHost

@Composable
fun InventoryApp(
    windowSize: WindowSizeClass,
    navController: NavHostController = rememberNavController()
) {
    val width = windowSize.widthSizeClass
    if (width == WindowWidthSizeClass.Compact) {

        InventoryNavHost(
            navController = navController,
            windowSize = windowSize
        )
    } else {
        // para pantallas mas grandes despues de telefono
        Row(Modifier.fillMaxSize()) {


            Surface(
                modifier = Modifier
                    .width(88.dp)
                    .fillMaxHeight(),
                tonalElevation = 1.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(vertical = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top
                ) {
                    IconButton(onClick = { }) {
                        Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Notas")
                    }
                    Spacer(Modifier.height(8.dp))
                    Text("Notas")
                }
            }

            Scaffold { inner ->
                Box(
                    Modifier
                        .padding(inner)
                        .fillMaxSize()
                ) {
                    InventoryNavHost(
                        navController = navController,
                        windowSize = windowSize
                    )
                }
            }
        }
    }
}


@Composable
fun InventoryTopAppBar(
    title: String,
    canNavigateBack: Boolean,
    modifier: Modifier = Modifier,
    navigateUp: () -> Unit = {}
) {

    Surface(tonalElevation = 1.dp) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Atrás"
                    )
                }
            }
            Spacer(Modifier.width(4.dp))
            Text(text = title)
        }
    }
}
