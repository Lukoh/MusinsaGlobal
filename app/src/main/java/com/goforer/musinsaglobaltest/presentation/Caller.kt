package com.goforer.musinsaglobaltest.presentation

import android.content.Context
import android.content.Intent
import com.goforer.musinsaglobaltest.presentation.ui.MainActivity

object Caller {
    private const val SERVICE_PREPARING = "SERVICE_PREPARING"

    internal const val NOT_AVAILABLE_NETWORK_INTERNET = "NOT_AVAILABLE_NETWORK_INTERNET"

    internal fun runNotAvailableNetwork(context: Context) {
        val intent = Intent(context, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            putExtra(NOT_AVAILABLE_NETWORK_INTERNET, true)
        }

        context.startActivity(intent)
    }

    internal fun preparingService(context: Context) {
        val intent = Intent(context, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            putExtra(SERVICE_PREPARING, true)
        }

        context.startActivity(intent)
    }
}