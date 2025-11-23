package com.example.inventory.ui.item

import android.net.Uri
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalContext

@Composable
fun VideoPlayer(
    videoUri: Uri,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    // Crear ExoPlayer
    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            val mediaItem = MediaItem.fromUri(videoUri)
            setMediaItem(mediaItem)
            prepare()
        }
    }

    // Liberar el player cuando el composable salga de pantalla
    DisposableEffect(
        AndroidView(
            modifier = modifier,
            factory = { ctx ->
                com.google.android.exoplayer2.ui.PlayerView(ctx).apply {
                    player = exoPlayer
                }
            }
        )
    ) {
        onDispose {
            exoPlayer.release()
        }
    }
}
