package com.example.inventory.ui.item

import android.content.Context
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.inventory.InventoryTopAppBar
import com.example.inventory.R
import com.example.inventory.data.Note
import com.example.inventory.ui.AppViewModelProvider
import com.example.inventory.ui.notes.NoteDetailsViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.platform.LocalContext
import com.example.inventory.ui.media.MediaViewModel
import kotlinx.coroutines.launch

fun getMimeType(uri: Uri, context: Context): String? {
    val contentResolver = context.contentResolver
    return contentResolver.getType(uri)
}

@Composable

fun NoteDetailsScreen(
    noteId: Int,
    navigateBack: () -> Unit,
    navigateToEditNote: (Int) -> Unit,
    navigateToReminderList: (Int, String) -> Unit,  // ⬅️ AGREGA ESTO
    modifier: Modifier = Modifier,
    viewModel: NoteDetailsViewModel = viewModel(factory = AppViewModelProvider.Factory)
)
{

    val context = LocalContext.current
    val note by viewModel.noteDetails.collectAsState()
    var deleteConfirmationRequired by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    val mediaViewModel: MediaViewModel = viewModel(factory = AppViewModelProvider.Factory)
    val mediaList by mediaViewModel.mediaList.collectAsState()

    // Cargar multimedia de esta nota
    LaunchedEffect(noteId) {
        mediaViewModel.loadMediaForNote(noteId)
    }

    Scaffold(
        topBar = {
            InventoryTopAppBar(
                title = stringResource(R.string.note_detail_title),
                canNavigateBack = true,
                navigateUp = navigateBack
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navigateToEditNote(noteId) },
                shape = MaterialTheme.shapes.medium
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = stringResource(R.string.edit_item_title)
                )
            }
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp)
        ) {

            note?.let {
                NoteDetails(
                    note = it,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // ⭐ BOTÓN PARA VER RECORDATORIOS ⭐
            if (note != null) {
                Button(
                    onClick = { navigateToReminderList(noteId, note!!.title) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = null,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text("Ver Recordatorios")
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Archivos adjuntos",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(mediaList) { media ->
                    when (media.type) {
                        "image" -> {
                            Image(
                                painter = rememberAsyncImagePainter(
                                    model = Uri.parse(media.uri)
                                ),
                                contentDescription = "Imagen",
                                modifier = Modifier.size(130.dp)
                            )
                        }

                        "audio" -> {
                            AudioPlayer(audioUri = Uri.parse(media.uri))
                        }

                        "video" -> {
                            VideoPlayer(videoUri = Uri.parse(media.uri))
                        }
                    }
                }
            }

            OutlinedButton(
                onClick = { deleteConfirmationRequired = true },
                shape = MaterialTheme.shapes.small,
                modifier = Modifier
                    .padding(top = 24.dp)
                    .fillMaxWidth()
            ) {
                Text(stringResource(R.string.delete))
            }
        }
    }

    if (deleteConfirmationRequired) {
        DeleteConfirmationDialog(
            onDeleteConfirm = {
                deleteConfirmationRequired = false
                coroutineScope.launch {
                    viewModel.deleteNote()
                    navigateBack()
                }
            },
            onDeleteCancel = { deleteConfirmationRequired = false }
        )
    }
}

@Composable
private fun DeleteConfirmationDialog(
    onDeleteConfirm: () -> Unit,
    onDeleteCancel: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDeleteCancel,
        title = { Text("Eliminar nota") },
        text = { Text("¿Seguro que deseas eliminar esta nota?") },
        confirmButton = {
            TextButton(onClick = onDeleteConfirm) {
                Text("Eliminar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDeleteCancel) {
                Text("Cancelar")
            }
        }
    )
}

@Composable
fun NoteDetails(
    note: Note,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(16.dp)
    ) {
        Text(
            text = note.title,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = note.content,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}
