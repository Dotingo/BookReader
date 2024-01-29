package com.example.bookreader

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable

fun TextScreen(content: String, title: String, navigateBack:()->Unit) {
    val lines = content.split('\n')
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(title = { Text(text = title) },
                navigationIcon = {
                    IconButton(onClick = {navigateBack()}) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = null
                        )
                    }
                })
        }
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 5.dp)
                .background(MaterialTheme.colorScheme.background),
            contentPadding = it

        ) {
            items(lines.size) {index ->
                Log.d("m", content)
                Text(
                    text = lines[index],
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }
    }

}
