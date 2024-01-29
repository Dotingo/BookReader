package com.example.bookreader.data

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class File(

    val uri: String,
    val title: String,
    val size: String,

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
)
