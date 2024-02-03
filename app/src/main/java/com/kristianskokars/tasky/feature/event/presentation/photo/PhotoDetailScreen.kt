package com.kristianskokars.tasky.feature.event.presentation.photo

import android.os.Parcelable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.kristianskokars.tasky.R
import com.kristianskokars.tasky.core.domain.model.Photo
import com.kristianskokars.tasky.core.presentation.components.ScreenSurface
import com.kristianskokars.tasky.core.presentation.theme.Black
import com.kristianskokars.tasky.core.presentation.theme.White
import com.kristianskokars.tasky.nav.AppGraph
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.result.EmptyResultBackNavigator
import com.ramcosta.composedestinations.result.ResultBackNavigator
import kotlinx.parcelize.Parcelize

@Parcelize
data class DeletePhoto(val photo: Photo): Parcelable

@AppGraph
@Destination
@Composable
fun PhotoDetailScreen(
    photo: Photo,
    resultNavigator: ResultBackNavigator<DeletePhoto?>
) {
    PhotoDetailScreenContent(photo = photo, resultNavigator = resultNavigator)
}

@Composable
private fun PhotoDetailScreenContent(
    photo: Photo,
    resultNavigator: ResultBackNavigator<DeletePhoto?>
) {
    Scaffold(
        topBar = {
            PhotoTopBar(
                onCloseClick = { resultNavigator.navigateBack(null) },
                onDeleteClick = { resultNavigator.navigateBack(DeletePhoto(photo)) }
            )
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(horizontal = 16.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            AsyncImage(
                model = photo.url,
                modifier = Modifier
                    .clip(RoundedCornerShape(5.dp))
                    .fillMaxSize(),
                contentDescription = null
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PhotoTopBar(
    modifier: Modifier = Modifier,
    onCloseClick: () -> Unit,
    onDeleteClick: () -> Unit,
) {
    TopAppBar(
        modifier = modifier,
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Black),
        navigationIcon = {
            IconButton(onClick = onCloseClick) {
                Icon(
                    modifier = Modifier.size(24.dp),
                    painter = painterResource(id = R.drawable.ic_close),
                    contentDescription = stringResource(id = R.string.go_back)
                )
            }
        },
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(R.string.photo),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
        },
        actions = {
            IconButton(onClick = onDeleteClick) {
                Icon(
                    modifier = Modifier.size(24.dp),
                    painter = painterResource(R.drawable.ic_delete),
                    contentDescription = stringResource(R.string.delete_photo),
                    tint = White
                )
            }
        }
    )
}

@Preview
@Composable
private fun PhotoDetailScreenPreview() {
    ScreenSurface {
        PhotoDetailScreenContent(
            photo = Photo("", ""),
            resultNavigator = EmptyResultBackNavigator()
        )
    }
}
