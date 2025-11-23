// File: NotesScreen.kt
package com.example.inventory.ui.notes

import android.net.Uri
import android.widget.VideoView
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.inventory.R
import com.example.inventory.data.Note
import com.example.inventory.ui.AppViewModelProvider
import com.example.inventory.ui.item.AudioPlayer
import com.example.inventory.ui.item.VideoPlayer
import com.example.inventory.ui.item.getMimeType
import com.example.inventory.ui.navigation.NavigationDestination
import java.text.SimpleDateFormat
import java.util.*

object NotesDestination : NavigationDestination {
    override val route = "notes"
    override val titleRes = R.string.app_name
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesScreen(
    navigateToNoteEntry: () -> Unit,
    navigateToNoteDetails: (Int, String) -> Unit
){
    val viewModel: NotesViewModel = viewModel(factory = AppViewModelProvider.Factory)
    val notesUiState by viewModel.notesUiState.collectAsState()
    var isReminderView by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(if (isReminderView) "Recordatorios" else "Notas")
                },
                actions = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(end = 16.dp)
                    ) {
                        Switch(
                            checked = isReminderView,
                            onCheckedChange = { isReminderView = it }
                        )
                    }

                    IconButton(onClick = { navigateToNoteEntry() }) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = null)
                    }
                }
            )
        }
    ) { padding ->

        val filteredNotes = if (isReminderView) {
            notesUiState.notes.filter { it.fecha != 0L || it.hora != 0L }
        } else {
            notesUiState.notes.filter { it.fecha == 0L && it.hora == 0L }
        }

        NotesList(
            notes = filteredNotes,
            onNoteClick = { id, title ->
                navigateToNoteDetails(id, title)
            },
            modifier = Modifier.padding(padding)
        )
    }
}

@Composable
private fun NotesList(
    notes: List<Note>,
    onNoteClick: (Int, String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier.fillMaxSize()) {
        items(notes, key = { it.id }) { note ->
            NoteItem(note = note, onClick = { onNoteClick(note.id, note.title) })
        }
    }

}
@Composable
private fun NoteItem(
    note: Note,
    onClick: () -> Unit
) {
    val dateFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    val timeFormatter = SimpleDateFormat("HH:mm", Locale.getDefault())

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            Text(text = note.title, style = MaterialTheme.typography.titleLarge)

            Spacer(modifier = Modifier.height(4.dp))

            Text(text = note.content, style = MaterialTheme.typography.bodyMedium)

            if (note.fecha != 0L) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Fecha: ${dateFormatter.format(Date(note.fecha))}",
                    style = MaterialTheme.typography.bodySmall
                )
            }

            if (note.hora != 0L) {
                Text(
                    text = "Hora: ${timeFormatter.format(Date(note.hora))}",
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(note.multimediaUris) { uriString ->
                    val uri = Uri.parse(uriString)
                    val context = LocalContext.current
                    val mimeType = getMimeType(uri, context)

                    when {
                        mimeType?.startsWith("image/") == true -> {
                            Image(
                                painter = rememberAsyncImagePainter(model = uri),
                                contentDescription = null,
                                modifier = Modifier
                                    .width(100.dp)
                                    .height(150.dp)
                            )
                        }

                        mimeType?.startsWith("audio/") == true -> {
                            AudioPlayer(audioUri = uri)
                        }

                        mimeType?.startsWith("video/") == true -> {
                            VideoPlayer(videoUri = uri, modifier = Modifier.height(200.dp))
                        }

                        else -> {
                            Text("Formato no soportado")
                        }
                    }
                }
            }
        }
    }
}

