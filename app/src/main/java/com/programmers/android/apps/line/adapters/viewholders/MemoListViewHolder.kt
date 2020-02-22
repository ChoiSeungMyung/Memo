package com.programmers.android.apps.line.adapters.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.programmers.android.apps.line.R
import com.programmers.android.apps.line.databinding.ListMemoItemBinding
import com.programmers.android.apps.line.models.Memo
import kotlinx.android.synthetic.main.list_memo_item.view.*

class MemoListViewHolder(private val binding: ListMemoItemBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(listener: MemoItemClick, item: Memo) {
        binding.apply {
            memo = item
            if (item.memoImages.isNotEmpty()) {
                Glide.with(binding.root)
                    .load(item.memoImages[0]?.imageUrl)
                    .centerCrop()
                    .error(R.drawable.ic_camera_undo)
                    .into(this.root.memoThumb)
            }

            clickListener = View.OnClickListener {
                listener.onClick(item.memoId)
            }
        }
    }
}

interface MemoItemClick {
    fun onClick(id: Int)
}