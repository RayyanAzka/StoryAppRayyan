package com.dicoding.picodiploma.loginwithanimation.view.customview

import android.content.Context
import android.graphics.Canvas
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatEditText

class MyEditText : AppCompatEditText, View.OnTouchListener {

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        // Menambahkan hint pada editText
        hint = "Masukkan teks"

        // Menambahkan text aligmnet pada editText
        textAlignment = View.TEXT_ALIGNMENT_VIEW_START
    }

    private fun init() {

        // Menambahkan aksi ketika ada perubahan text akan memunculkan clear button
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // Do nothing.
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                error = if(s != null && s.length < 8){
                    "Password kurang dari 8 karakter"
                } else {
                    null
                }
            }

            override fun afterTextChanged(s: Editable) {
                // Do nothing.
            }
        })
    }

    override fun onTouch(v: View?, event: MotionEvent): Boolean {
        return false
    }
}