package com.kevin.timenote.ui.countdown

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kevin.timenote.base.LocalNavController
import com.kevin.timenote.base.LocalToast
import com.kevin.timenote.common.util.TimeL.printD
import com.kevin.timenote.common.util.daysUntilTarget
import com.kevin.timenote.common.util.toFriendlyString
import com.kevin.timenote.domain.model.CountdownModel
import com.kevin.timenote.ui.theme.cornerCard
import com.kevin.timenote.ui.theme.spaceHeight10
import com.kevin.timenote.ui.theme.spaceHeight20
import com.kevin.timenote.ui.theme.uniformPadding
import com.kevin.timenote.ui.widget.TimeSwitch
import com.kevin.timenote.ui.widget.TimeTopBar
import com.nlf.calendar.Solar
import kotlinx.coroutines.delay
import java.util.Date
import kotlin.math.abs
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class, ExperimentalMaterial3Api::class)
@Composable
fun CountdownDetailScreen(viewModel: CountdownDetailViewModel = hiltViewModel()) {
    val navController = LocalNavController.current
    val detail by viewModel.uiState.collectAsStateWithLifecycle()
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = detail?.date?.takeIf { it > 0 } ?: System.currentTimeMillis()
    )
    val selectedDate = datePickerState.selectedDateMillis?.let {
        convertMillisToDate(it)
    } ?: ""
    val lunarDate =
        datePickerState.selectedDateMillis?.let { Solar.fromDate(Date(it)).lunar.toString() } ?: ""
    LaunchedEffect(detail?.date) {
        val serverDate = detail?.date
        if (serverDate != null && serverDate > 0) {
            // 只有当当前 Picker 的时间跟数据不一样时才更新，防止不必要的重绘
            if (datePickerState.selectedDateMillis != serverDate) {
                datePickerState.selectedDateMillis = serverDate
            }
        }
    }
    
    val context = LocalContext.current
    var showPermissionDialog by remember { mutableStateOf(false) }
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            viewModel.updateState { it.copy(remind = true) }
        }
    }

    Scaffold(topBar = {
        TimeTopBar(
            title = detail?.eventTypeName ?: "加载中..."
        )
    }) { contentPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding),
            contentAlignment = Alignment.Center
        ) {
            if (detail == null) {
                CircularProgressIndicator()
            } else {
                CountdownDetailContent(
                    detail!!,
                    selectedDate,
                    onDateClick = { showDatePicker = true },
                    onUpdateClick = {
                        viewModel.save { navController.popBackStack() }
                    },
                    onDeleteClick = {
                        viewModel.delete { navController.popBackStack() }
                    },
                    onUseLunarClick = { v ->
                        viewModel.updateState {
                            it.copy(
                                isLunar = v,
                                lunarDate = lunarDate
                            )
                        }
                    },
                    onRepeatModeChange = { mode ->
                        viewModel.updateState { it.copy(repeatMode = mode) }
                    },
                    onRemindChange = { v ->
                        if (v) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                if (ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
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
                )
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
                            datePickerState.selectedDateMillis?.let { date ->
                                viewModel.updateState {
                                    it.copy(date = date, lunarDate = lunarDate)
                                }
                            }
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

@OptIn(ExperimentalTime::class)
@Composable
fun CountdownDetailContent(
    model: CountdownEditUiState,
    selectedDate: String,
    onDateClick: () -> Unit,
    onUpdateClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onUseLunarClick: (b: Boolean) -> Unit,
    onRepeatModeChange: (com.kevin.timenote.domain.model.RepeatMode) -> Unit,
    onRemindChange: (Boolean) -> Unit
) {
    // 1. 定义一个状态来保存剩余的总秒数
    var remainingTime by remember { mutableLongStateOf(0L) }
    var repeatMenuExpanded by remember { mutableStateOf(false) }
    val targetMillis = model.date // 数据库存的时间戳
//    var useLunar =model.isLunar
// 3. 将总秒数拆分为天、时、分、秒
//    val days = remainingTime / (24 * 3600)
//    val hours = (remainingTime % (24 * 3600)) / 3600
//    val minutes = (remainingTime % 3600) / 60
//    val seconds = remainingTime % 60


    // 1. 初始化音效生成器 (DTMF_1 是类似“嘀”的短促音)
//    val toneGenerator = remember { ToneGenerator(AudioManager.STREAM_NOTIFICATION, 80) }
    // 2. 初始化震动器
//    val context = LocalContext.current
//    val vibrator = remember {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
//            val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
//            vibratorManager.defaultVibrator
//        } else {
//            context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
//        }
//    }
    val days = model.date.daysUntilTarget()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = uniformPadding),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    // 居中的数字 Box（核心）
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .background(
                                MaterialTheme.colorScheme.onPrimary,
                                shape = RoundedCornerShape(cornerCard)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "${abs(days)}",
                            fontSize = 50.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Box(  modifier = Modifier
                        .size(100.dp)
                        .offset(x =100.dp)
                        .background(
                            color = Color.Transparent,
                            shape = RoundedCornerShape(cornerCard)
                        ).padding(start = 10.dp),contentAlignment = Alignment.CenterStart){

                    Text(
                        text = "天",
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    }
                }

                Text(
                    if (days >= 0) "还有" else "已过去",
                    color = if (days >= 0) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.outline,
                    modifier = Modifier.padding(top = 10.dp),
                )
            }
        }



        Spacer(modifier = Modifier.height(24.dp))
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
                ) { onDateClick() },
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = uniformPadding)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) { onDateClick() },
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
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("农历")
                TimeSwitch(checked = model.isLunar, modifier = Modifier.scale(0.7f)) { v ->
                    onUseLunarClick(v)
                }
            }

            AnimatedVisibility(visible = model.isLunar) {
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
                        Text("农历日期")
                        Text(model.lunarDate)
                    }
                }
            }
        }
//        Spacer(Modifier.height(spaceHeight10))
//        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
//            CompositionLocalProvider(LocalMinimumInteractiveComponentSize provides 20.dp) {
//                Checkbox(
//                    checked = model.isLunar,
//                    onCheckedChange = { v ->
////                        useLunar = v
//                        onUseLunarClick(v)
//                    }
//                )
//                Text("使用农历")
//            }
//        }

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
                    checked = model.remind,
                    modifier = Modifier.scale(0.7f)
                ) { v ->
                    onRemindChange(v)
                }
            }

            // 只有当提醒打开时才显示重复设置
            AnimatedVisibility(visible = model.remind) {
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
                            Text(model.repeatMode.description)
                        }

                        DropdownMenu(
                            expanded = repeatMenuExpanded,
                            onDismissRequest = { repeatMenuExpanded = false }
                        ) {
                            com.kevin.timenote.domain.model.RepeatMode.values().forEach { mode ->
                                DropdownMenuItem(
                                    text = { Text(mode.description) },
                                    onClick = {
                                        onRepeatModeChange(mode)
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
        Row {
            Button(
                modifier = Modifier.weight(1f),
                onClick = {
//                    currentToast.showToast("修改成功")
                    onUpdateClick()
//                    viewModel.save {
//                        navController.popBackStack()
//                    }
                }) {
                Text("修改")
            }

            Spacer(Modifier.width(uniformPadding))
            Button(
                modifier = Modifier.weight(1f),
                onClick = {
                    onDeleteClick()
//                        viewModel.delete {
//                            navController.popBackStack()
//                        }
                }
            ) {
                Text("删除")
            }
        }
//        // 4. 显示带滚动动画的数字
//        Row(verticalAlignment = Alignment.CenterVertically) {
//            TimeSection(value = days.toInt(), unit = "天")
//            TimeSection(value = hours.toInt(), unit = "时")
//            TimeSection(value = minutes.toInt(), unit = "分")
//            TimeSection(value = seconds.toInt(), unit = "秒", isHighPriority = true)
//        }
    }
}

@Composable
fun TimeSection(value: Int, unit: String, isHighPriority: Boolean = false) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(8.dp)) {
        // 数字滚动动画
        AnimatedContent(
            targetState = value,
            transitionSpec = {
                // 新数字从底部滑入，旧数字向顶部滑出
                if (targetState < initialState) {
                    (slideInVertically { it } + fadeIn()) togetherWith (slideOutVertically { -it } + fadeOut())
                } else {
                    (slideInVertically { -it } + fadeIn()) togetherWith (slideOutVertically { it } + fadeOut())
                }.using(SizeTransform(clip = false))
            },
            label = "NumberScroll"
        ) { targetValue ->
            Text(
                text = String.format("%02d", targetValue),
                style = MaterialTheme.typography.displayMedium.copy(
                    fontFamily = FontFamily.Monospace, // 必须用等宽字体，防止抖动
                    fontWeight = FontWeight.Bold
                )
            )
        }

        // 呼吸灯效果（针对秒数单位）
        val alpha = if (isHighPriority) {
            val infiniteTransition = rememberInfiniteTransition(label = "Pulse")
            infiniteTransition.animateFloat(
                initialValue = 0.4f,
                targetValue = 1f,
                animationSpec = infiniteRepeatable(tween(1000), RepeatMode.Reverse),
                label = "Alpha"
            ).value
        } else 1f

        Text(
            text = unit,
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.alpha(alpha)
        )
    }
}

@Composable
fun RollingNumber(number: Int) {
    // AnimatedContent 会根据 targetState 的变化自动执行动画
    AnimatedContent(
        targetState = number,
        transitionSpec = {
            // 定义进入和退出的动画：新数字从上方滑入，旧数字向下方滑出
            (slideInVertically { it } + fadeIn()).togetherWith(slideOutVertically { -it } + fadeOut())
        },
        label = "NumberRolling"
    ) { targetNumber ->
        Text(
            text = "$targetNumber",
            style = MaterialTheme.typography.displayLarge,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun BreathingEffect() {
    // 1. 创建一个无限循环的转换动画
    val infiniteTransition = rememberInfiniteTransition(label = "Breathing")

    // 2. 定义缩放比例从 1.0f 到 1.2f 循环
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse // 关键：反转动画实现“呼吸”感
        ),
        label = "Scale"
    )

    Box(
        modifier = Modifier
            .size(100.dp)
            .graphicsLayer(scaleX = scale, scaleY = scale) // 应用缩放
            .background(Color.Red.copy(alpha = 0.2f), shape = CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Text("进行中", color = Color.Red)
    }
}
