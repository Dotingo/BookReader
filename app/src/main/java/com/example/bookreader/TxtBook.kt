package com.example.bookreader

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bookreader.presentation.FileState
import com.example.bookreader.presentation.FilesEvent

@Composable
fun TxtBook(
    state: FileState,
    index: Int,
    onEvent: (FilesEvent) -> Unit,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape = RoundedCornerShape(5.dp))
            .clickable(onClick = onClick)
    ) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
        ) {
            Image(
                modifier = Modifier.padding(vertical = 8.dp),
                painter = painterResource(id = R.drawable.txt_image),
                contentDescription = "Txt Image"
            )
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(0.9f)
            ) {
                Row {
                    Text(
                        text = state.files[index].title,
                        fontSize = 20.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Spacer(modifier = Modifier.size(5.dp))
                Row {
                    Text(text = state.files[index].size)
                }
            }
            Column(modifier = Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.Center) {
                IconButton(
                    onClick = { onEvent(FilesEvent.DeleteFile(state.files[index])) },

                    ) {
                    Icon(
                        ImageVector.vectorResource(R.drawable.ic_delete),
                        contentDescription = "Delete file"
                    )
                }

            }

        }
    }

}

@Preview
@Composable
fun TxtBookPreview() {
TxtBook(state = FileState(), index = 1, onEvent = {}) {

}
}