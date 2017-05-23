package com.github.kttinunf.playground

import android.text.TextWatcher
import android.widget.TextView

fun TextView.textChangedListener(init: _TextWatcher.() -> Unit) {
    val listener = _TextWatcher()
    listener.init()
    addTextChangedListener(listener)
}

class _TextWatcher : TextWatcher {

    private var _beforeTextChanged: ((CharSequence?, Int, Int, Int) -> Unit)? = null
    private var _onTextChanged: ((CharSequence?, Int, Int, Int) -> Unit)? = null
    private var _afterTextChanged: ((android.text.Editable?) -> Unit)? = null
    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        _beforeTextChanged?.invoke(s, start, count, after)
    }

    fun beforeTextChanged(listener: (CharSequence?, Int, Int, Int) -> Unit) {
        _beforeTextChanged = listener
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        _onTextChanged?.invoke(s, start, before, count)
    }

    fun onTextChanged(listener: (CharSequence?, Int, Int, Int) -> Unit) {
        _onTextChanged = listener
    }

    override fun afterTextChanged(s: android.text.Editable?) {
        _afterTextChanged?.invoke(s)
    }

    fun afterTextChanged(listener: (android.text.Editable?) -> Unit) {
        _afterTextChanged = listener
    }

}
