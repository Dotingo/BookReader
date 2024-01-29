package com.example.bookreader.presentation

import android.net.Uri
import com.example.bookreader.data.File

sealed interface FilesEvent {
    object GetFiles: FilesEvent

    data class DeleteFile(val file: File): FilesEvent

    data class SaveFile(
        val uri: String,
        val title: String,
        val size: String
    ): FilesEvent
}