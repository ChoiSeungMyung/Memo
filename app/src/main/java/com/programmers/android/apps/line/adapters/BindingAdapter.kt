package com.programmers.android.apps.line.adapters

import android.widget.TextView
import androidx.databinding.BindingAdapter

@BindingAdapter("memoTitle")
fun bindMemoTitle(view: TextView, title: String) {
    view.text = title
}

@BindingAdapter("memoText")
fun bindMemoText(view: TextView, text: String) {
    view.text = text
}