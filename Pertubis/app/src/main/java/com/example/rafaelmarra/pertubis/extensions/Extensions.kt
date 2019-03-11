package com.example.rafaelmarra.pertubis.extensions

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import com.redmadrobot.inputmask.MaskedTextChangedListener

fun EditText.applyTextMask(format: String) {
    MaskedTextChangedListener.installOn(this, format,
        object : MaskedTextChangedListener.ValueListener {
            override fun onTextChanged(maskFilled: Boolean, extractedValue: String) {
                Log.d("TAG", extractedValue)
                Log.d("TAG", maskFilled.toString())
            }
        })
}

fun hideKeyboard(activity: Activity) {
    val view = activity.currentFocus
    view?.let { v ->
        val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.hideSoftInputFromWindow(v.windowToken, 0)
    }
}