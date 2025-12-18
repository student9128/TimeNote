package com.kevin.timenote.base

import android.R

interface UIActions {
    fun showToast(message: String)
    fun showSnackBar(message: String)
    fun showLoading(show: Boolean,loadingContent: String="正在加载...")
}