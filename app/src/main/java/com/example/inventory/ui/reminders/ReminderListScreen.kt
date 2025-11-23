package com.example.inventory.ui.reminders

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.inventory.ui.AppViewModelProvider
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReminderListScreen(
    noteId: Int,
    noteTitle: String,
    navigateBack: () -> Unit,
    reminderViewModel: ReminderViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {


    // 🔥 Cargar recordatorios de esa nota
    LaunchedEffect(noteId) {
        reminderViewModel.load(noteId)
    }

    val reminders by reminderViewModel.reminders.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Recordatorios de: $noteTitle") },
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier.padding(padding).padding(16.dp)
        ) {

            if (reminders.isEmpty()) {
                Text("No hay recordatorios.", style = MaterialTheme.typography.bodyLarge)
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(reminders) { reminder ->

                        ReminderItem(
                            date = reminder.timeMillis,
                            onDelete = {
                                reminderViewModel.deleteReminder(reminder.id)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ReminderItem(
    date: Long,
    onDelete: () -> Unit
) {
    val format = SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.getDefault())

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = format.format(Date(date)))
            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Eliminar recordatorio"
                )
            }
        }
    }
}
