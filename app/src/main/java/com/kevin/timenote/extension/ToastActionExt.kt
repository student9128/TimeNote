package com.kevin.timenote.extension

import com.kevin.timenote.base.UIActions

/**
 * // how to use：
 * scope.launch {
 *     uiActions.runWithLoading {
 *         api.submitData()
 *     }
 * }
 */
suspend fun <T> UIActions.runWithLoading(
    block: suspend () -> T,
    loadingContent: String = "正在加载..."
): T {
    try {
        showLoading(true, loadingContent)
        return block()
    } finally {
        showLoading(false, loadingContent) // 无论成功失败，都会关闭 Loading
    }
}
