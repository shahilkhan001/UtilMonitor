package com.example.utilmonitor

import android.annotation.SuppressLint
import android.content.Context

object PreferenceHelper {
    private const val PREF_NAME = "usage_limits"

    @SuppressLint("UseKtx")
    fun setLimit(context: Context, key: String, value: Int) {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit().putInt(key, value).apply()
    }

    fun getLimit(context: Context, key: String): Int {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).getInt(key, -1)
    }
}
