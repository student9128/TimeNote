package com.kevin.timenote.common.util

import android.util.Log
import com.kevin.timenote.BuildConfig
import timber.log.Timber

object TimeL {
    const val GLOBAL_TAG = "TimeNote"

    var enabled: Boolean = BuildConfig.DEBUG

    inline fun printD(
        tag: String = GLOBAL_TAG,
        message: () -> String
    ) {
        if (!enabled) return
        Log.d(tag, message())
    }

    inline fun printI(
        tag: String = GLOBAL_TAG,
        message: () -> String
    ) {
        if (!enabled) return
        Log.i(tag, message())
    }

    inline fun printW(
        tag: String = GLOBAL_TAG,
        message: () -> String
    ) {
        if (!enabled) return
        Log.w(tag, message())
    }

    inline fun printE(
        tag: String = GLOBAL_TAG,
        throwable: Throwable? = null,
        message: () -> String
    ) {
        if (!enabled) return
        Log.e(tag, message(), throwable)
    }

    inline fun printTimberD(message: () -> String) {
        if (!enabled) return
        Timber.d(message())
    }

    inline fun printTimberI(message: () -> String) {
        if (!enabled) return
        Timber.i(message())
    }

    inline fun printTimberW(message: () -> String) {
        if (!enabled) return
        Timber.w(message())
    }

    inline fun printTimberE(message: () -> String) {
        if (!enabled) return
        Timber.e(message())
    }

}