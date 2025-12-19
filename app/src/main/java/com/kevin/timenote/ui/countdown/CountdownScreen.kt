package com.kevin.timenote.ui.countdown

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kevin.timenote.base.LocalNavController
import com.kevin.timenote.ui.theme.AppTextFieldColors
import com.kevin.timenote.ui.theme.TimeNoteTheme
import com.kevin.timenote.ui.theme.cornerCard
import com.kevin.timenote.ui.theme.cornerTextField
import com.kevin.timenote.ui.theme.spaceHeight
import com.kevin.timenote.ui.theme.uniformPadding
import com.kevin.timenote.ui.widget.TimeTopBar
import com.nlf.calendar.Solar
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CountdownScreen(
    viewModel: CountdownEditViewModel = hiltViewModel()
) {
    val navController = LocalNavController.current
    val state by viewModel.uiState.collectAsState()
    val eventTypes by viewModel.eventTypes.collectAsStateWithLifecycle()
    var selectedEventIndex by remember { mutableStateOf(0) }
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = System.currentTimeMillis()
    )
    val selectedDate = datePickerState.selectedDateMillis?.let {
        convertMillisToDate(it)
    } ?: ""
    val lunarDate =
        datePickerState.selectedDateMillis?.let { Solar.fromDate(Date(it)).lunar.toString() } ?: ""
    Scaffold(topBar = { TimeTopBar(title = "添加") }) { contentPadding ->
        Box(
            modifier = Modifier
                .padding(contentPadding)
                .padding(uniformPadding)
        ) {
            Column {
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(eventTypes) { model ->
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(5.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = true,
                                onCheckedChange = {  } // 当用户点击时更新状态
                            )
                            Box(
                                Modifier
                                    .background(Color(model.color), shape = CircleShape)
                                    .width(16.dp)
                                    .height(16.dp)
                            )
                            Text(model.name)
                        }
                    }
                }
                TextField(
                    value = state.title,
                    onValueChange = viewModel::updateTitle,
                    placeholder = { Text("事件") },
                    shape = RoundedCornerShape(cornerTextField),
                    colors = AppTextFieldColors(),
                    modifier = Modifier.fillMaxWidth()
                )


                Spacer(Modifier.height(spaceHeight))
                Column(
                    modifier = Modifier
                        .background(
                            MaterialTheme.colorScheme.onPrimary, shape = RoundedCornerShape(
                                cornerCard
                            )
                        )
                        .padding(uniformPadding)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = uniformPadding)
                            .clickable { showDatePicker = true },
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("目标日")
                        Text(selectedDate)
                    }
                    HorizontalDivider()
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = uniformPadding),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("农历日期")
                        Text(lunarDate)
                    }
                }



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
            if (showDatePicker) {
                val confirmEnabled = remember {
                    derivedStateOf { datePickerState.selectedDateMillis != null }
                }
                DatePickerDialog(
                    onDismissRequest = { showDatePicker = false },
                    confirmButton = {
                        TextButton(onClick = {
                            showDatePicker = false
                            datePickerState.selectedDateMillis?.let { viewModel.updateDate(it) }
                        }, enabled = confirmEnabled.value) {
                            Text("确定")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showDatePicker = false }) { Text("取消") }
                    },
                ) { DatePicker(state = datePickerState) }
            }
        }
    }

}

fun convertMillisToDate(millis: Long): String {
    val formatter = SimpleDateFormat("MM-dd-yyyy", Locale.getDefault())
    return formatter.format(Date(millis))
}

@Composable
fun Te(v: String = "", modifier: Modifier = Modifier) {
    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.primary)
            .padding(10.dp)
    ) {
        Text("nihao")
        TextField(
            value = "$v",
            onValueChange = {},
            placeholder = { Text("事件") },
            shape = RoundedCornerShape(cornerTextField),
            colors = AppTextFieldColors(),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview(showBackground = true)
@Composable
fun Test(modifier: Modifier = Modifier) {
    TimeNoteTheme {
        Te("nihao")
    }
}
