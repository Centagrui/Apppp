package com.example.inventory.ui.reminders

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inventory.data.Reminder
import com.example.inventory.data.ReminderRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ReminderViewModel(
    private val repository: ReminderRepository
) : ViewModel() {

    private val _reminders = MutableStateFlow<List<Reminder>>(emptyList())
    val reminders: StateFlow<List<Reminder>> = _reminders.asStateFlow()

    fun load(noteId: Int) {
        viewModelScope.launch {
            repository.getRemindersByNoteId(noteId).collect {
                _reminders.value = it
            }
        }
    }

    fun addReminder(noteId: Int, timeMillis: Long) {
        viewModelScope.launch {
            repository.insertReminder(Reminder(noteId = noteId, timeMillis = timeMillis))
        }
    }

    fun deleteReminder(id: Int) {
        viewModelScope.launch {
            repository.deleteReminder(id)
        }
    }
}
