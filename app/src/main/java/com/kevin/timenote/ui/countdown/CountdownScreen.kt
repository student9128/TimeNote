package com.kevin.timenote.ui.countdown

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController

@Composable
fun CountdownScreen(
    navController: NavController,
    viewModel: CountdownEditViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    Column(modifier = Modifier.padding(16.dp)) {
        TextField(
            value = state.title,
            onValueChange = viewModel::updateTitle,
            label = { Text("标题") }
        )

        Spacer(Modifier.height(16.dp))

        Button(onClick = {
            viewModel.save {
                navController.popBackStack()
            }
        }) {
            Text("保存")
        }

        if (state.isEditMode) {
            Spacer(Modifier.height(8.dp))
            Button(
                onClick = {
                    viewModel.delete {
                        navController.popBackStack()
                    }
                }
            ) {
                Text("删除")
            }
        }
    }
}
