package com.example.news.core.designsystem.component

import androidx.annotation.DrawableRes
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.news.core.designsystem.icon.Icons

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBar(
    titleRes: String,
    @DrawableRes navigationIcon: Int,
    navigationIconContentDescription: String,
    @DrawableRes actionIcon: Int,
    actionIconContentDescription: String,
    modifier: Modifier = Modifier,
    colors: TopAppBarColors = TopAppBarDefaults.centerAlignedTopAppBarColors(),
    onNavigationClick: () -> Unit = {},
    onActionClick: () -> Unit = {},
) {
    CenterAlignedTopAppBar(
        title = { Text(text = titleRes) },
        /*title = {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
//                                    if (!isSearch) {
                Text(
                    "Now in ${
                        categorySelected.lowercase().replaceFirstChar {
                            if (it.isLowerCase()) it.titlecase() else it.toString()
                        }
                    }",
//                                        maxLines = 1,
//                                            overflow = TextOverflow.Ellipsis,
//                                            style = MaterialTheme.typography.titleLarge,
//                                            fontWeight = FontWeight.SemiBold,
//                                            modifier = Modifier.clickable {
//                                                isShowBottomSheet = true
//                                            },
                )
//                                    IconButton(onClick = {
//                                        isShowBottomSheet = true
//                                    }) {
//                                        Icon(
//                                            painter = painterResource(id = Icons.KeyboardArrowDown),
//                                            contentDescription = "Change category"
//                                        )
//                                    }
//                                    } else {
//                                        TextField(
//                                            value = searchQuery,
//                                            placeholder = {
//                                                Text(text = "Search in $categorySelected ..")
//                                            },
//                                            onValueChange = {
//                                                searchQuery = it
//                                            },
//                                            keyboardOptions = KeyboardOptions(
//                                                imeAction = ImeAction.Search,
//                                            ),
//                                            colors = TextFieldDefaults.colors(
//                                                focusedContainerColor = Color.Transparent,
//                                                unfocusedContainerColor = Color.Transparent,
//                                                disabledContainerColor = Color.Transparent,
//                                                focusedIndicatorColor = Color.Transparent,
//                                                unfocusedIndicatorColor = Color.Transparent,
//                                                disabledIndicatorColor = Color.Transparent,
//                                            ),
//                                        )
//                                    }
            }
        },*/
        navigationIcon = {
            IconButton(onClick = onNavigationClick) {
                Icon(
                    painter = painterResource(id = navigationIcon),
                    contentDescription = navigationIconContentDescription,
                    tint = MaterialTheme.colorScheme.onSurface,
                )
            }
//            IconButton(onClick = {
//                isSearch = !isSearch
//                searchQuery = ""
////                                    homeViewModel.onEvent(HomeViewUiEvent.SearchArticle(search = ""))
//            }) {
//                Icon(
//                    painter = if (!isSearch) {
//                        painterResource(id = Icons.Search)
//                    } else {
//                        painterResource(id = Icons.Clear)
//                    },
//                    contentDescription = "Localized description",
//                    tint = MaterialTheme.colorScheme.primary,
//                )
//            }
        },
        actions = {
            IconButton(onClick = onActionClick) {
                Icon(
                    painter = painterResource(id = actionIcon),
                    contentDescription = actionIconContentDescription,
                    tint = MaterialTheme.colorScheme.onSurface,
                )
            }
        },
        colors = colors,
        modifier = modifier.testTag("topAppBar"),
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview("Top App Bar")
@Composable
private fun TopAppBarPreview() {
    TopAppBar(
        titleRes = stringResource(id = android.R.string.untitled),
        navigationIcon = Icons.Search,
        navigationIconContentDescription = "Navigation icon",
        actionIcon = Icons.MoreVertical,
        actionIconContentDescription = "Action icon",
    )
}
