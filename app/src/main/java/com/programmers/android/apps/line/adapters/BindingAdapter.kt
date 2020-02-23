package com.programmers.android.apps.line.adapters

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.programmers.android.apps.line.extensions.loge
import com.programmers.android.apps.line.models.MemoImage

@BindingAdapter("memoTitle")
fun bindMemoTitle(view: TextView, title: String) {
    view.text = title
}

@BindingAdapter("memoText")
fun bindMemoText(view: TextView, text: String) {
    view.text = text
}