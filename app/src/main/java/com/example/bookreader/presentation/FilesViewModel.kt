package com.example.bookreader.presentation

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookreader.data.File
import com.example.bookreader.data.FileDao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FilesViewModel(
    private val dao: FileDao
) : ViewModel() {

    private var files = dao.getFiles()

    private val _state = MutableStateFlow(FileState())
    val state =
        combine(_state, files){state, files ->
            state.copy(
                files = files
            )
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), FileState())

    fun onEvent(event: FilesEvent) {
        when (event) {
            is FilesEvent.DeleteFile -> {
                viewModelScope.launch {
                    dao.deleteFile(event.file)
                }
            }
            FilesEvent.GetFiles -> {
                dao.getFiles()
            }
            is FilesEvent.SaveFile -> {
                val file = File(
                        uri = state.value.uri.value,
                        title = state.value.title.value,
                        size = state.value.size.value
                    )

                viewModelScope.launch {
                    dao.insertFile(file)
                }

                _state.update {
                    it.copy(
                        uri = mutableStateOf(""),
                        title = mutableStateOf(""),
                        size = mutableStateOf(""),
                    )
                }
            }
        }
    }

}