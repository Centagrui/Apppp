package com.example.inventory.ui.item

import android.content.Context
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.example.inventory.ui.item.AudioPlayer
import com.example.inventory.ui.item.VideoPlayer
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import kotlinx.coroutines.launch
import androidx.compose.ui.platform.LocalContext

fun getMimeType(uri: Uri, context: Context): String? {
    val contentResolver = context.contentResolver
    return contentResolver.getType(uri)
}


@Composable
fun NoteDetailsScreen(
    noteId: Int,
    navigateBack: () -> Unit,
    navigateToEditNote: (Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: NoteDetailsViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {

    val context = LocalContext.current


    val note by viewModel.noteDetails.collectAsState()
    var deleteConfirmationRequired = remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

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
        },
        content = { innerPadding ->
            Box(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    note?.let {
                        NoteDetails(
                            note = it,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    note?.multimediaUris?.let { multimediaList ->
                        LazyColumn(modifier = Modifier.height(150.dp)) {
                            items(multimediaList) { uri ->
                                val mimeType = getMimeType(Uri.parse(uri), context)
                                when {
                                    mimeType?.startsWith("image/") == true -> {
                                        Image(
                                            painter = rememberAsyncImagePainter(model = Uri.parse(uri)),
                                            contentDescription = "Imagen multimedia",
                                            modifier = Modifier
                                                .width(100.dp)
                                                .height(150.dp)
                                        )
                                    }
                                    mimeType?.startsWith("audio/") == true -> {
                                        AudioPlayer(audioUri = Uri.parse(uri))
                                    }
                                    mimeType?.startsWith("video/") == true -> {
                                        VideoPlayer(videoUri = Uri.parse(uri), modifier = Modifier.height(200.dp))
                                    }
                                    else -> {
                                        Text("Formato no soportado")
                                    }
                                }
                            }
                        }
                    }

                    // Para eliminar una nota
                    OutlinedButton(
                        onClick = { deleteConfirmationRequired.value = true },
                        shape = MaterialTheme.shapes.small,
                        modifier = Modifier
                            .padding(top = 16.dp)
                            .fillMaxWidth()
                    ) {
                        Text(stringResource(R.string.delete))
                    }
                }
            }
        }
    )

    // Para ver ssi si lo quiere eliminar
    if (deleteConfirmationRequired.value) {
        DeleteConfirmationDialog(
            onDeleteConfirm = {
                deleteConfirmationRequired.value = false
                coroutineScope.launch {
                    viewModel.deleteNote()
                    navigateBack()
                }
            },
            onDeleteCancel = { deleteConfirmationRequired.value = false }
        )
    }
}

@Composable
private fun DeleteConfirmationDialog(
    onDeleteConfirm: () -> Unit,
    onDeleteCancel: () -> Unit,
    modifier: Modifier = Modifier
) {

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
