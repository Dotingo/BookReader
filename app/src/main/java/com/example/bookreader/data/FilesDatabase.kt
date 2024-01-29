package com.example.bookreader.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [File::class],
    version = 1
)
abstract class FilesDatabase: RoomDatabase() {
    abstract val dao: FileDao
}