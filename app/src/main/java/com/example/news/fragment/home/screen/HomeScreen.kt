package com.example.news.fragment.home.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.example.news.domain.model.Article
import com.example.news.ui.component.ErrorButton
import com.example.news.ui.component.LoadingCircular

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    articleList: LazyPagingItems<Article>? = null,
) {
    val context = LocalContext.current
    val backgroundColor = MaterialTheme.colorScheme.background.toArgb()

    if (articleList == null) return
    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Adaptive(minSize = 100.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        items(articleList.itemCount) { index ->
            Card(
                onClick = {
                    // TODO onclick
//                    launchCustomChromeTab(
//                        context,
//                        Uri.parse(articleList[index]?.url),
//                        backgroundColor
//                    )
                },
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            ) {
                Column {
                    // TODO load image
//                    if (!articleList[index]?.urlToImage.isNullOrEmpty()) {
//                        Row {
//                            NewsResourceHeaderImage(articleList[index]?.urlToImage)
//                        }
//                    }
                    Box(
                        modifier = Modifier.padding(16.dp),
                    ) {
                        Column {
                            Spacer(modifier = Modifier.height(12.dp))
                            Row {
                                Text(
                                    articleList[index]?.title ?: "-",
                                    style = MaterialTheme.typography.headlineSmall,
                                )
                                Spacer(modifier = Modifier.weight(1f))
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    articleList[index]?.publishedAt ?: "-",
                                    style = MaterialTheme.typography.labelSmall,
                                )
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                        }
                    }
                }
            }
        }
        articleList.apply {
            item(
                span = {
                    GridItemSpan(maxLineSpan)
                }
            ) {
                when {
                    loadState.refresh is LoadState.Loading || loadState.append is LoadState.Loading -> {
                        LoadingCircular(
                            modifier = Modifier.fillMaxWidth(),
                        )
                    }

                    loadState.refresh is LoadState.Error || loadState.append is LoadState.Error -> {
                        ErrorButton(
                            modifier = Modifier.fillMaxWidth(),
                            text = "Error get data ..",
                            onClick = {
                                retry()
                            }
                        )
                    }
                }
            }
        }
    }
}

//@Composable
//fun NewsResourceHeaderImage(
//    headerImageUrl: String?,
//) {
//    var isLoading by remember { mutableStateOf(true) }
//    var isError by remember { mutableStateOf(false) }
//    val imageLoader = rememberAsyncImagePainter(
//        model = headerImageUrl,
//        onState = { state ->
//            isLoading = state is AsyncImagePainter.State.Loading
//            isError = state is AsyncImagePainter.State.Error
//        },
//    )
//    val isLocalInspection = LocalInspectionMode.current
//    Box(
//        modifier = Modifier
//            .fillMaxWidth()
//            .height(180.dp),
//        contentAlignment = Alignment.Center,
//    ) {
//        if (isLoading) {
//            // Display a progress bar while loading
//            CircularProgressIndicator(
//                modifier = Modifier
//                    .align(Alignment.Center)
//                    .size(80.dp),
//                color = MaterialTheme.colorScheme.tertiary,
//            )
//        }
//
//        Image(
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(180.dp),
//            contentScale = ContentScale.Crop,
//            painter = if (isError.not() && !isLocalInspection) {
//                imageLoader
//            } else {
//                painterResource(drawable.ic_placeholder_default)
//            },
//            // TODO b/226661685: Investigate using alt text of  image to populate content description
//            contentDescription = null, // decorative image,
//        )
//    }
//}

//fun launchCustomChromeTab(context: Context, uri: Uri, @ColorInt toolbarColor: Int) {
//    val customTabBarColor = CustomTabColorSchemeParams.Builder()
//        .setToolbarColor(toolbarColor).build()
//    val customTabsIntent = CustomTabsIntent.Builder()
//        .setDefaultColorSchemeParams(customTabBarColor)
//        .build()
//
//    customTabsIntent.launchUrl(context, uri)
//}