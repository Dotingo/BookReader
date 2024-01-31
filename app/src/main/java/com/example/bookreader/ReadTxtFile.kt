package com.example.bookreader

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.OpenableColumns
import android.text.format.Formatter
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.bookreader.presentation.FileState
import com.example.bookreader.presentation.FilesEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.InputStream


suspend fun readTextFromUri(context: Context, uri: Uri): String = withContext(Dispatchers.IO) {
    var inputStream: InputStream? = null
    try {
        inputStream = context.contentResolver.openInputStream(uri)
        return@withContext inputStream?.bufferedReader()?.use { it.readText() } ?: ""
    } finally {
        inputStream?.close()
    }
}

private fun getFileInfo(context: Context, uri: Uri): Pair<String, String> {
    val contentResolver = context.contentResolver
    val cursor = contentResolver.query(uri, null, null, null, null, null)

    var fileName = ""
    var fileSize = ""

    cursor?.use {
        if (it.moveToFirst()) {
            val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            fileName = it.getString(nameIndex)

            val sizeIndex = it.getColumnIndex(OpenableColumns.SIZE)
            val size = it.getLong(sizeIndex)
            fileSize = Formatter.formatFileSize(context, size)
        }
    }
    return Pair(fileName, fileSize)
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChooseTxtFile(
    state: FileState,
    onEvent: (FilesEvent) -> Unit
) {

    val context = LocalContext.current
    val navController = rememberNavController()
    var uri by remember { mutableStateOf<Uri?>(null) }
    var content by remember { mutableStateOf("") }
    var title by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    val saveDb by remember {
        mutableStateOf(true)
    }
    val chooseFileLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            scope.launch {
                if (result.resultCode == Activity.RESULT_OK) {
                    result.data?.data?.let { data ->
                        uri = data
                        content = uri?.let { readTextFromUri(context, it) }.toString()

                        val fileInfo = getFileInfo(context, uri!!)
                        state.title.value = fileInfo.first
                        state.size.value = fileInfo.second
                        state.uri.value = uri.toString()
                        title = fileInfo.first

                        context.contentResolver.takePersistableUriPermission(
                            uri!!,
                            Intent.FLAG_GRANT_READ_URI_PERMISSION
                        )

                        onEvent(
                            FilesEvent.SaveFile(
                                title = fileInfo.first,
                                size = fileInfo.second,
                                uri = uri.toString()
                            )
                        )

                        navController.navigate("textScreen")
                    }
                }
            }

        }

    LaunchedEffect(saveDb) {

    }

    val listState = rememberLazyListState()
    val expandedFab by remember {
        derivedStateOf {
            listState.firstVisibleItemIndex == 0
        }
    }

    NavHost(navController, startDestination = "main") {
        composable("main") {
            Scaffold(
                floatingActionButton = {
                    ExtendedFloatingActionButton(
                        onClick = {
                            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                                addCategory(Intent.CATEGORY_OPENABLE)
                                type = "text/plain"
                            }
                            chooseFileLauncher.launch(intent)
                        },
                        expanded = expandedFab,
                        icon = { Icon(Icons.Filled.Add, "") },
                        text = { Text(text = "Выбрать файл") },
                    )
                },
                floatingActionButtonPosition = FabPosition.End,
                topBar = {
                    CenterAlignedTopAppBar(
                        title = { Text(text = "Book Reader") }
                    )
                }

            ) {
                LazyColumn(
                    state = listState,
                    contentPadding = it,
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background)
                        .padding(horizontal = 2.dp)
                ) {
                    items(state.files.size) { index ->
                        TxtBook(state = state, index = index, onEvent = onEvent) {
                            val navigateUri = state.files[index].uri
                            scope.launch {
                                title = state.files[index].title
                                content = readTextFromUri(context, Uri.parse(navigateUri))
                                navController.navigate("textScreen")
                            }

                        }
                        Spacer(modifier = Modifier.size(2.dp))
                    }

                }
            }
        }
        composable("textScreen") {
            TextScreen(content, title) {
                if (navController.currentBackStackEntry?.lifecycle?.currentState
                    == Lifecycle.State.RESUMED
                ) {
                    navController.popBackStack()
                }
            }
        }
    }
}
