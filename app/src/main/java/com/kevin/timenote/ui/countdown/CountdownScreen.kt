package com.kevin.timenote.ui.countdown

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LocalMinimumInteractiveComponentEnforcement
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kevin.timenote.base.LocalNavController
import com.kevin.timenote.base.LocalToast
import com.kevin.timenote.common.util.formatWithPattern
import com.kevin.timenote.domain.model.RepeatMode
import com.kevin.timenote.ui.navigation.TimeRoute
import com.kevin.timenote.ui.theme.AppTextFieldColors
import com.kevin.timenote.ui.theme.cornerCard
import com.kevin.timenote.ui.theme.cornerTextField
import com.kevin.timenote.ui.theme.spaceHeight
import com.kevin.timenote.ui.theme.spaceHeight10
import com.kevin.timenote.ui.theme.spaceHeight20
import com.kevin.timenote.ui.theme.uniformPadding
import com.kevin.timenote.ui.widget.TimeSwitch
import com.kevin.timenote.ui.widget.TimeTopBar
import com.nlf.calendar.Lunar
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
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val eventTypes by viewModel.eventTypes.collectAsStateWithLifecycle()
    var showDatePicker by remember { mutableStateOf(false) }
    var repeatMenuExpanded by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = state.date
    )
    val isSaveEnabled = state.title.isNotBlank()
    val currentToast = LocalToast.current

    val context = LocalContext.current
    var showPermissionDialog by remember { mutableStateOf(false) }
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            viewModel.updateState { it.copy(remind = true) }
        }
    }
    LaunchedEffect(datePickerState.selectedDateMillis) {
        datePickerState.selectedDateMillis?.let { date->
            viewModel.updateState { it.copy(date = date, lunarDate = "${Lunar.fromDate(Date(date))}") }
        }
    }

    Scaffold(topBar = { TimeTopBar(title = if (!state.isEditMode) "添加" else "修改") }) { contentPadding ->
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
                    itemsIndexed(eventTypes) { index, model ->
                        val isSelected = state.eventTypeName == model.name
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(5.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) {
//                                viewModel.updateEventTypeName(model.name)
                                viewModel.updateState {
                                    it.copy(
                                        eventTypeName = model.name,
                                        eventTypeColor = model.color
                                    )
                                }
                            }
                        ) {
                            CompositionLocalProvider(LocalMinimumInteractiveComponentSize provides 20.dp) {
                                Checkbox(
                                    modifier = Modifier,
                                    checked = isSelected,
                                    colors = CheckboxDefaults.colors(
                                        checkedColor = Color(model.color),
                                        checkmarkColor = Color.White,
                                        uncheckedColor = Color(model.color)
                                    ),
                                    onCheckedChange = null //only read mode
                                )
                            }
                            Text(model.name)
                        }
                    }
                }
                TextField(
                    value = state.title,
                    onValueChange = viewModel::updateTitle,
                    placeholder = { Text("在这里添加事件...") },
                    shape = RoundedCornerShape(cornerTextField),
                    colors = AppTextFieldColors(),
                    modifier = Modifier.fillMaxWidth()
                )


                Spacer(Modifier.height(spaceHeight))
                Column(
                    modifier = Modifier
                        .animateContentSize()
                        .background(
                            MaterialTheme.colorScheme.onPrimary, shape = RoundedCornerShape(
                                cornerCard
                            )
                        )
                        .padding(uniformPadding)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) { showDatePicker = true },
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = uniformPadding)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) { showDatePicker = true },
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("目标日")
                        Text(state.date.formatWithPattern("yyyy年MM月dd日"))
                    }
                    HorizontalDivider()
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = uniformPadding),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("农历")
                        TimeSwitch(checked = state.isLunar, modifier = Modifier.scale(0.7f)) { v ->
                            viewModel.updateState {
                                it.copy(
                                    isLunar = v,
                                    lunarDate = state.lunarDate
                                )
                            }
                        }
                    }
                    AnimatedVisibility(visible = state.isLunar) {
                        // 因为 AnimatedVisibility 内部只能有一个直接子组件（或者需要 Column 包裹），
                        // 这里我们将 Divider 和 Row 包裹在一个 Column 中
                        Column {
                            HorizontalDivider()
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = uniformPadding),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("农历日")
                                Text(state.lunarDate)
                            }
                        }
                    }
                }

                Spacer(Modifier.height(spaceHeight10))

                // 重复提醒设置
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            MaterialTheme.colorScheme.onPrimary, shape = RoundedCornerShape(
                                cornerCard
                            )
                        )
                        .padding(uniformPadding)
                ) {
                    // 提醒开关
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = uniformPadding),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("提醒")
                        TimeSwitch(
                            checked = state.remind,
                            modifier = Modifier.scale(0.7f)
                        ) { v ->
                            if (v) {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                    if (ContextCompat.checkSelfPermission(
                                            context,
                                            Manifest.permission.POST_NOTIFICATIONS
                                        ) == PackageManager.PERMISSION_GRANTED
                                    ) {
                                        viewModel.updateState { it.copy(remind = true) }
                                    } else {
                                        showPermissionDialog = true
                                    }
                                } else {
                                    viewModel.updateState { it.copy(remind = true) }
                                }
                            } else {
                                viewModel.updateState { it.copy(remind = false) }
                            }
                        }
                    }

                    // 只有当提醒打开时才显示重复设置
                    AnimatedVisibility(visible = state.remind) {
                        Column {
                            HorizontalDivider()
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { repeatMenuExpanded = true }
                                    .padding(vertical = uniformPadding)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text("重复提醒")
                                    Text(state.repeatMode.description)
                                }

                                DropdownMenu(
                                    expanded = repeatMenuExpanded,
                                    onDismissRequest = { repeatMenuExpanded = false }
                                ) {
                                    RepeatMode.values().forEach { mode ->
                                        DropdownMenuItem(
                                            text = { Text(mode.description) },
                                            onClick = {
                                                viewModel.updateRepeatMode(mode)
                                                repeatMenuExpanded = false
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(Modifier.height(spaceHeight20))
                Row(modifier = Modifier.fillMaxWidth()) {
                    Button(modifier = Modifier.weight(1f), enabled = isSaveEnabled, onClick = {
                        currentToast.showToast("保存成功")
                        viewModel.save {
                            navController.popBackStack()
                        }
                    }) {
                        Text("保存")
                    }

                    if (state.isEditMode) {
                        Spacer(Modifier.width(uniformPadding))
                        Button(
                            modifier = Modifier.weight(1f),
                            onClick = {
                                viewModel.delete {
                                    navController.popBackStack<TimeRoute.Home>(inclusive = false)
//                                    navController.navigate(TimeRoute.Home) {
//                                        popUpTo<TimeRoute.Home> { inclusive = false }
//                                        launchSingleTop = true
//                                    }
//                                    navController.popBackStack()
                                }
                            }
                        ) {
                            Text("删除")
                        }
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
//                            datePickerState.selectedDateMillis?.let { date ->
//                                viewModel.updateState {
//                                    it.copy(date = date, lunarDate = state.lunarDate)
//                                }
//                            }
                        }, enabled = confirmEnabled.value) {
                            Text("确定")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showDatePicker = false }) { Text("取消") }
                    },
                ) { DatePicker(state = datePickerState) }
            }

            if (showPermissionDialog) {
                AlertDialog(
                    onDismissRequest = { showPermissionDialog = false },
                    title = { Text("开启通知权限") },
                    text = { Text("开启通知权限后，才能在设定时间收到提醒。") },
                    confirmButton = {
                        TextButton(onClick = {
                            showPermissionDialog = false
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                            }
                        }) {
                            Text("去开启")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showPermissionDialog = false }) {
                            Text("取消")
                        }
                    }
                )
            }
        }
    }

}

fun convertMillisToDate(millis: Long): String {
    val formatter = SimpleDateFormat("yyyy年MM月dd日", Locale.getDefault())
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
        Checkbox(
            modifier = Modifier.background(
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primary
            ),
            checked = true,
            onCheckedChange = { isChecked ->
                // Update the individual child state
            }
        )
//        TextField(
//            value = "$v",
//            onValueChange = {},
//            placeholder = { Text("事件") },
//            shape = RoundedCornerShape(cornerTextField),
//            colors = AppTextFieldColors(),
//            modifier = Modifier.fillMaxWidth()
//        )
    }
}