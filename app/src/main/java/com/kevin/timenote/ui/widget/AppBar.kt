package com.kevin.timenote.ui.widget

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.kevin.timenote.base.LocalNavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(
    title: String,
    modifier: Modifier = Modifier,
    showBackIcon: Boolean = true,
    onBackClick: (() -> Unit)? = null
) {
    val navController: NavHostController = rememberNavController()
    CenterAlignedTopAppBar(
        title = { Text(text = title, maxLines = 1, overflow = TextOverflow.Ellipsis) },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
        ),
        navigationIcon = {
            if (showBackIcon) {
                IconButton(onClick = { onBackClick?.invoke() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Localized description"
                    )
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimeTopBar(
    modifier: Modifier = Modifier,
    title: String = "",
    subTitle: String = "",
    showSearch: Boolean = false,
    showBackIcon: Boolean = true,
    onBackClick: (() -> Unit)? = null,
    onSearchClick: () -> Unit = {},
) {
    val current = LocalNavController.current
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary,
        ),
        navigationIcon = {
            if (showBackIcon) {
                IconButton(
                    onClick = {
                        if (onBackClick == null) {
                            current.popBackStack()
                        } else {
                            onBackClick.invoke()
                        }
                    },
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Localized description",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        },
        title = {
            Column {
                Text(title)
                if (subTitle.isNotEmpty()) {
                    Text(subTitle, fontSize = 12.sp)
                }
            }
        },
        actions = {
            if (showSearch) {
                IconButton(onClick = onSearchClick) {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = "Share items"
                    )
                }
            }
        },
    )
}