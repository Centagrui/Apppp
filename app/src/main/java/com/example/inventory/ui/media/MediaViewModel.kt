package com.example.inventory.ui.media

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.inventory.data.Media
import com.example.inventory.data.MediaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MediaViewModel(
    private val mediaRepository: MediaRepository
) : ViewModel() {

    private val _mediaList = MutableStateFlow<List<Media>>(emptyList())
    val mediaList: StateFlow<List<Media>> = _mediaList.asStateFlow()

    fun loadMediaForNote(noteId: Int) {
        viewModelScope.launch {
            mediaRepository.getMediaForNote(noteId).collect { media ->
                _mediaList.value = media
            }
        }
    }
}
