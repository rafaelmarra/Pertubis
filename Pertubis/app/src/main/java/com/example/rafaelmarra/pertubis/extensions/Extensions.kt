package com.example.rafaelmarra.pertubis.extensions

import android.util.Log
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