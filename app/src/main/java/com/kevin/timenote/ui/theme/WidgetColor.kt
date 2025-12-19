package com.kevin.timenote.ui.theme

import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun AppTextFieldColors(): TextFieldColors {
    return TextFieldDefaults.colors(
        // 1. 文本颜色 (Text Colors)
        focusedTextColor = MaterialTheme.colorScheme.onSurface,
        unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
        disabledTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
        errorTextColor = MaterialTheme.colorScheme.error,

        // 2. 容器背景色 (Container Colors)
        // 推荐使用 surfaceContainerHighest 作为输入框背景，能最好地适配动态壁纸
        focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
        disabledContainerColor = MaterialTheme.colorScheme.surfaceContainerHighest.copy(alpha = 0.38f),
        errorContainerColor = MaterialTheme.colorScheme.surfaceContainerHighest,

        // 3. 光标颜色 (Cursor Colors)
        cursorColor = MaterialTheme.colorScheme.primary,
        errorCursorColor = MaterialTheme.colorScheme.error,

        // 4. 文本选择手柄/背景颜色 (Selection Colors)
        selectionColors = TextSelectionColors(
            handleColor = MaterialTheme.colorScheme.primary,



            backgroundColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f)
        ),

        // 5. 指示器/下划线颜色 (Indicator Colors)
        // 聚焦时用 Primary 强调，未聚焦用 Variant 弱化，去下划线设为 Transparent
        focusedIndicatorColor =  Color.Transparent,
        unfocusedIndicatorColor = Color.Transparent,
        disabledIndicatorColor =  Color.Transparent,
        errorIndicatorColor =  Color.Transparent,

        // 6. 前置图标颜色 (Leading Icon Colors)
        focusedLeadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
        unfocusedLeadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
        disabledLeadingIconColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
        errorLeadingIconColor = MaterialTheme.colorScheme.error,

        // 7. 后置图标颜色 (Trailing Icon Colors)
        focusedTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
        unfocusedTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
        disabledTrailingIconColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
        errorTrailingIconColor = MaterialTheme.colorScheme.error,

        // 8. 标签颜色 (Label Colors) - 浮动上去的那个标题
        focusedLabelColor = MaterialTheme.colorScheme.primary, // 聚焦时高亮
        unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
        disabledLabelColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
        errorLabelColor = MaterialTheme.colorScheme.error,

        // 9. 占位符颜色 (Placeholder Colors) - 内容为空时显示的提示
        focusedPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
        unfocusedPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
        disabledPlaceholderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
        errorPlaceholderColor = MaterialTheme.colorScheme.error,

        // 10. 辅助文本颜色 (Supporting Text Colors) - 底部的小字提示
        focusedSupportingTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
        unfocusedSupportingTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
        disabledSupportingTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
        errorSupportingTextColor = MaterialTheme.colorScheme.error,

        // 11. 前缀/后缀文本颜色 (Prefix/Suffix Colors)
        focusedPrefixColor = MaterialTheme.colorScheme.onSurfaceVariant,
        unfocusedPrefixColor = MaterialTheme.colorScheme.onSurfaceVariant,
        disabledPrefixColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
        errorPrefixColor = MaterialTheme.colorScheme.onSurfaceVariant,

        focusedSuffixColor = MaterialTheme.colorScheme.onSurfaceVariant,
        unfocusedSuffixColor = MaterialTheme.colorScheme.onSurfaceVariant,
        disabledSuffixColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
        errorSuffixColor = MaterialTheme.colorScheme.onSurfaceVariant
    )
}