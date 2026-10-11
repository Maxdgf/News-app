package com.maxdgf.newspaper.presentation.all_news_screen.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import com.maxdgf.newspaper.R
import kotlinx.coroutines.CancellationException

@Composable
fun ArticleItem(
    sourceName: String,
    title: String,
    urlToImage: String?,
    onClick: () -> Unit,
    onGetInfo: () -> Unit,
    onSaveArticle: () -> Unit,
    onShareArticle: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp),
        elevation = CardDefaults.elevatedCardElevation(3.dp),
        shape = RoundedCornerShape(30.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .clickable(onClick = onClick)
        ) {
            val context = LocalContext.current
            val imageRequest = remember(urlToImage) {
                return@remember ImageRequest.Builder(context)
                    .data(urlToImage)
                    .size(500, 250)
                    .build()
            }

            AsyncImage(
                model = imageRequest,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1.0f),
                placeholder = painterResource(R.drawable.article_image_loading),
                error = painterResource(R.drawable.article_no_image),
                contentScale = ContentScale.Crop,
                onError = { errorState ->
                    if (errorState.result.throwable is CancellationException)
                        return@AsyncImage
                },
                contentDescription = title
            )

            Spacer(modifier = Modifier.height(10.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            ) {
                Text(
                    text = title,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 2
                )

                HorizontalDivider()

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = sourceName,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 15.sp
                    )

                    Spacer(modifier = Modifier.weight(1.0f))

                    IconButton(onClick = onSaveArticle) {
                        Icon(
                            painter = painterResource(R.drawable.baseline_save_alt_24),
                            contentDescription = null
                        )
                    }

                    IconButton(onClick = onShareArticle) {
                        Icon(
                            painter = painterResource(R.drawable.outline_share_24),
                            contentDescription = null
                        )
                    }

                    IconButton(onClick = onGetInfo) {
                        Icon(
                            painter = painterResource(R.drawable.outline_info_24),
                            contentDescription = null
                        )
                    }
                }
            }
        }
    }
}