package com.example.inventory.ui.item

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.inventory.InventoryTopAppBar
import com.example.inventory.ui.AppViewModelProvider
import com.example.inventory.ui.reminders.ReminderViewModel
import com.example.inventory.utils.ReminderScheduler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteEntryScreen(
    navigateBack: () -> Unit,
    viewModel: NoteEntryViewModel = viewModel(factory = AppViewModelProvider.Factory),
    reminderViewModel: ReminderViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {

    val noteUiState = viewModel.noteUiState  // ← YA NO ES FLOW

    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }
    var multimediaUris by remember { mutableStateOf<List<String>>(emptyList()) }

    val context = LocalContext.current
    var selectedReminderTime by remember { mutableStateOf<Long?>(null) }


    // ---------- SELECT MULTIMEDIA ----------
    val pickImage = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            multimediaUris = multimediaUris + uri.toString()
            viewModel.updateMultimediaUris(multimediaUris)
        }
    }

    var capturedMediaUri by remember { mutableStateOf<Uri?>(null) }

    val takePictureLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success && capturedMediaUri != null) {
            multimediaUris = multimediaUris + capturedMediaUri.toString()
            viewModel.updateMultimediaUris(multimediaUris)
        }
    }

    val captureVideoLauncher = rememberLauncherForActivityResult(ActivityResultContracts.CaptureVideo()) { success ->
        if (success && capturedMediaUri != null) {
            multimediaUris = multimediaUris + capturedMediaUri.toString()
            viewModel.updateMultimediaUris(multimediaUris)
        }
    }


    Scaffold(
        topBar = {
            InventoryTopAppBar(
                title = "Agregar Nota",
                canNavigateBack = true,
                navigateUp = navigateBack
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {

            // TÍTULO
            OutlinedTextField(
                value = noteUiState.noteDetails.title,
                onValueChange = { viewModel.updateTitle(it) },
                label = { Text("Título") },
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            )

            // CONTENIDO
            OutlinedTextField(
                value = noteUiState.noteDetails.content,
                onValueChange = { viewModel.updateContent(it) },
                label = { Text("Contenido") },
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            )

            // ----- FECHA Y HORA -----
            Button(
                onClick = { showDatePicker = true },
                modifier = Modifier.padding(16.dp)
            ) { Text("Seleccionar Fecha para Recordatorio") }

            Button(
                onClick = { showTimePicker = true },
                modifier = Modifier.padding(16.dp)
            ) { Text("Seleccionar Hora para Recordatorio") }

            Text(
                text = selectedReminderTime?.let {
                    SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date(it))
                } ?: "Recordatorio no seleccionado",
                modifier = Modifier.padding(start = 16.dp)
            )


            // ---------- SELECCIÓN DE IMAGEN/VIDEO ----------
            Button(
                onClick = { pickImage.launch("image/*") },
                modifier = Modifier.padding(16.dp)
            ) {
                Text("Seleccionar Imagen")
            }

            Button(
                onClick = { pickImage.launch("video/*") },
                modifier = Modifier.padding(16.dp)
            ) {
                Text("Seleccionar Video")
            }

            if (multimediaUris.isNotEmpty()) {
                LazyColumn(
                    modifier = Modifier.padding(16.dp).height(200.dp)
                ) {
                    items(multimediaUris) { uri ->
                        Image(
                            painter = rememberAsyncImagePainter(Uri.parse(uri)),
                            contentDescription = null,
                            modifier = Modifier
                                .padding(8.dp)
                                .size(120.dp)
                        )
                    }
                }
            }


            // ---------- BOTÓN GUARDAR ----------
            Button(
                onClick = {

                    CoroutineScope(Dispatchers.IO).launch {

                        // 1) Guardar nota y obtener ID REAL
                        val newNoteId = viewModel.saveNoteAndReturnId()

                        // 2) Guardar recordatorio (si se seleccionó)
                        if (selectedReminderTime != null) {

                            reminderViewModel.addReminder(
                                noteId = newNoteId,
                                timeMillis = selectedReminderTime!!
                            )

                            ReminderScheduler.scheduleReminder(
                                context = context,
                                noteId = newNoteId,
                                noteTitle = noteUiState.noteDetails.title,
                                timeMillis = selectedReminderTime!!
                            )
                        }

                        // 3) Volver a UI
                        withContext(Dispatchers.Main) {
                            navigateBack()
                        }
                    }

                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                enabled = viewModel.isEntryValid
            ) {
                Text("Guardar Nota + Recordatorio")
            }
        }
    }


    // ---------- DATE PICKER ----------
    if (showDatePicker) {
        val calendar = Calendar.getInstance()
        DatePickerDialog(
            context,
            { _, y, m, d ->
                calendar.set(y, m, d)
                selectedReminderTime = calendar.timeInMillis
                showDatePicker = false
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    // ---------- TIME PICKER ----------
    if (showTimePicker) {
        val calendar = Calendar.getInstance()
        TimePickerDialog(
            context,
            { _, h, min ->
                if (selectedReminderTime != null) {
                    val cal = Calendar.getInstance()
                    cal.timeInMillis = selectedReminderTime!!
                    cal.set(Calendar.HOUR_OF_DAY, h)
                    cal.set(Calendar.MINUTE, min)
                    selectedReminderTime = cal.timeInMillis
                }
                showTimePicker = false
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true
        ).show()
    }
}
