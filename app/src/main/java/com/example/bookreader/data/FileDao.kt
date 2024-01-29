package com.example.bookreader.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FileDao {

    @Insert
    suspend fun insertFile(file: File)

    @Delete
    suspend fun deleteFile(file: File)

    @Query("SELECT * FROM file")
    fun getFiles(): Flow<List<File>>

}