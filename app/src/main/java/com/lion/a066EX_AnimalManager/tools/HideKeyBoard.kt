package com.lion.a066EX_AnimalManager.tools

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText

//edit text에 입력하다가 다른곳을 터치하면 키보드가내려감
class HideKeyBoard {

    fun setupHideKeyboardOnTouch(view: View) {
        if (view !is EditText) {
            view.setOnTouchListener { _, _ ->
                view.clearFocus()
                hideKeyBoard(view)
                false
            }
        }
        if (view is ViewGroup) {
            for (i in 0 until view.childCount) {
                val child = view.getChildAt(i)
                setupHideKeyboardOnTouch(child)
            }
        }
    }

    private fun hideKeyBoard(view: View) {
        val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}