package com.programmers.android.apps.line.adapters.viewholders

import android.view.View
import android.view.WindowId
import androidx.recyclerview.widget.RecyclerView
import com.programmers.android.apps.line.databinding.ListMemoItemBinding
import com.programmers.android.apps.line.extensions.loge
import com.programmers.android.apps.line.models.Memo
import kotlinx.android.synthetic.main.list_memo_item.view.*

class MemoListViewHolder(private val binding: ListMemoItemBinding): RecyclerView.ViewHolder(binding.root) {
    fun bind(listener: MemoItemClick, item: Memo) {
        binding.apply {
            memo = item
            clickListener = View.OnClickListener {
                loge("${item.memoId}, ${item.memoTitle}, ${item.memoText}")
                listener.onClick(item.memoId)
            }
        }
    }
}

interface MemoItemClick {
    fun onClick(id: Int)
}