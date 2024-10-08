package com.kristianskokars.tasky.feature.event.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.kristianskokars.tasky.R
import com.kristianskokars.tasky.core.domain.model.Photo
import com.kristianskokars.tasky.core.presentation.theme.Gray
import com.kristianskokars.tasky.core.presentation.theme.LightBlue
import com.kristianskokars.tasky.core.presentation.theme.LightGray
import com.kristianskokars.tasky.lib.fillParentWidth

@Composable
fun PhotosSection(
    photos: List<Photo> = emptyList(),
    isEditing: Boolean,
    onAddPhotoClick: () -> Unit,
    onPhotoClick: (Photo) -> Unit,
) {
    if (photos.isEmpty()) {
        AddPhotosText(onAddPhotoClick)
    } else {
        PhotosGallery(
            photos = photos,
            isEditing = isEditing,
            onAddPhotoClick = onAddPhotoClick,
            onPhotoClick = onPhotoClick
        )
    }
}

@Composable
private fun AddPhotosText(
    onAddPhotoClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillParentWidth(parentPadding = 16.dp)
            .clickable(onClick = onAddPhotoClick)
            .background(LightGray)
            .height(112.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_add),
            contentDescription = null,
            tint = Gray
        )
        Spacer(modifier = Modifier.size(12.dp))
        Text(
            text = stringResource(R.string.add_photos),
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp,
            color = Gray
        )
    }
}

@Composable
private fun PhotosGallery(
    photos: List<Photo>,
    isEditing: Boolean,
    onPhotoClick: (Photo) -> Unit,
    onAddPhotoClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillParentWidth(parentPadding = 16.dp)
            .background(LightGray)
            .padding(20.dp)
            .height(112.dp),
        verticalArrangement = Arrangement.Center,
    ) {
        Text(text = stringResource(R.string.photos), fontWeight = FontWeight.SemiBold, fontSize = 20.sp)
        Spacer(modifier = Modifier.size(20.dp))
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(photos, key = { it.key }) { photo ->
                PhotoCard(photo, onPhotoClick = onPhotoClick)
            }
            if (isEditing) {
                item {
                    AddPhotosCard(onAddPhotoClick = onAddPhotoClick)
                }
            }
        }
    }
}

@Composable
private fun PhotoCard(
    photo: Photo,
    contentDescription: String? = null,
    onPhotoClick: (Photo) -> Unit,
) {
    ClickableCard(
        clickLabel = stringResource(R.string.view_photo),
        onClick = { onPhotoClick(photo) }
    ) {
        AsyncImage(
            modifier = Modifier.fillMaxSize(),
            model = photo.url,
            contentDescription = contentDescription,
            alignment = Alignment.Center,
            contentScale = ContentScale.Crop,

        )
    }
}

@Composable
private fun AddPhotosCard(
    onAddPhotoClick: () -> Unit = {},
) {
    ClickableCard(clickLabel = stringResource(R.string.add_photos), onClick = onAddPhotoClick) {
        Icon(
            modifier = Modifier.align(Alignment.Center),
            painter = painterResource(id = R.drawable.ic_add),
            contentDescription = stringResource(R.string.add_photos),
            tint = LightBlue
        )
    }
}

@Composable
private fun ClickableCard(
    clickLabel: String,
    onClick: () -> Unit,
    content: @Composable BoxScope.() -> Unit,
) {
    Box(
        modifier = Modifier
            .border(2.dp, LightBlue, shape = RoundedCornerShape(4.dp))
            .clip(shape = RoundedCornerShape(4.dp))
            .size(64.dp)
            .clickable(
                role = Role.Button,
                onClickLabel = clickLabel,
                onClick = onClick
            )
    ) {
        content()
    }
}
