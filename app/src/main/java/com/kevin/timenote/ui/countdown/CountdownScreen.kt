package com.kevin.timenote.ui.countdown

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.kevin.timenote.base.LocalNavController
import com.kevin.timenote.ui.widget.TimeTopBar

@Composable
fun CountdownScreen(
    viewModel: CountdownEditViewModel = hiltViewModel()
) {
    val navController = LocalNavController.current
    val state by viewModel.uiState.collectAsState()
    Scaffold(topBar = { TimeTopBar(title = "添加") }) { contentPadding ->
        Column(modifier = Modifier.padding(contentPadding)) {
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

}
