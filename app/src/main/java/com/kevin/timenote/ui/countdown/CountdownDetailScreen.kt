package com.kevin.timenote.ui.countdown

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.kevin.timenote.ui.widget.TimeTopBar

@Composable
fun CountdownDetailScreen(modifier: Modifier = Modifier) {
    Scaffold(topBar = { TimeTopBar(title = "事件名称") }) { contentPadding ->
        Column(modifier = Modifier.padding(contentPadding)) {
            Text("CountdownDetailScreen")
        }
    }
}