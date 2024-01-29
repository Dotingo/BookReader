package com.example.bookreader

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.example.bookreader.data.FilesDatabase
import com.example.bookreader.presentation.FilesViewModel
import com.example.bookreader.ui.theme.BookReaderTheme

    class MainActivity : ComponentActivity() {

        private val database by lazy {
            Room.databaseBuilder(
                applicationContext,
                FilesDatabase::class.java,
                "files.db"
            ).build()
        }

        private val viewModel by viewModels<FilesViewModel> (
            factoryProducer = {
                object : ViewModelProvider.Factory {
                    override fun <T: ViewModel> create(modelClass: Class<T>): T{
                        return FilesViewModel(database.dao) as T
                    }
                }
            }
        )

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContent {
                BookReaderTheme {
                    val state by viewModel.state.collectAsState()
                    ChooseTxtFile(
                        state = state,
                        onEvent = viewModel::onEvent
                    )
                }
            }
        }

    }

