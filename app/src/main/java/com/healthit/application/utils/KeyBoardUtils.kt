package com.healthit.application.utils

import android.app.Activity
import android.content.Context
import android.os.IBinder
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.EditText

object KeyBoardUtils {

    private const val TAG: String = "Keyboard_util"

    fun hideKeyboard(activity: Activity) {
        try {
            val view = activity.window.currentFocus ?: return
            val inputManager = activity
                .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.hideSoftInputFromWindow(
                view.windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS
            )
        } catch (e: Exception) {
            Log.e(TAG, e.toString())
        }
    }

    fun closeKeyboard(c: Context, windowToken: IBinder?) {
        val mgr = c.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        mgr.hideSoftInputFromWindow(windowToken, 0)
    }

    fun openKeyboardWhenFocus(context: Context, editable: EditText) {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(editable, InputMethodManager.SHOW_IMPLICIT)
    }
}