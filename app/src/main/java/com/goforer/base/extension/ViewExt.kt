package com.goforer.base.extension

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.SystemClock
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.LinearLayout

class SafeClickListener(
    private val interval: Long,
    private inline val onSafeCLick: (View) -> Unit
) : View.OnClickListener {
    private var lastTimeClicked: Long = 0

    override fun onClick(v: View) {
        if (SystemClock.elapsedRealtime() - lastTimeClicked > interval) {
            lastTimeClicked = SystemClock.elapsedRealtime()
            onSafeCLick(v)
        }
    }
}

fun Window.setSystemBarTextDark() {
    val view = findViewById<View>(android.R.id.content)
    val flags = view.systemUiVisibility
    view.systemUiVisibility = flags or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
}


fun View.show(): View {
    if (visibility != View.VISIBLE) {
        visibility = View.VISIBLE
    }

    return this
}

/**
 * Hide the view. (visibility = View.INVISIBLE)
 */
fun View.hide(): View {
    if (visibility != View.INVISIBLE) {
        visibility = View.INVISIBLE
    }

    return this
}

/**
 * Remove the view (visibility = View.GONE)
 */
fun View.gone(): View {
    if (visibility != View.GONE) {
        visibility = View.GONE
    }

    return this
}


inline fun View.setSafeOnClickListener(interval: Long = 1000, crossinline onSafeClick: (View) -> Unit) {
    val safeClickListener = SafeClickListener(interval = interval) {
        onSafeClick(it)
    }

    setOnClickListener(safeClickListener)
}

fun Dialog.setDefaultWindowTheme() {
    window?.apply {
        setLayout(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
        addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        statusBarColor = Color.TRANSPARENT

        setSystemBarTextDark()
        setDimAmount(0.3f)
    }
}