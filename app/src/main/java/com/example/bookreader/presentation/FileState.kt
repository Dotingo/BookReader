package com.example.bookreader.presentation

import android.net.Uri
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.example.bookreader.data.File

data class FileState(

    val files: List<File> = emptyList(),
    val uri: MutableState<String> = mutableStateOf(""),
    val title: MutableState<String> = mutableStateOf(""),
    val size: MutableState<String> = mutableStateOf("")
)
