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
//                이미지가 있을때 첫번째 사진으로 썸네일 보여주기
                Glide.with(binding.root)
                    .load(item.memoImages[0]?.imageUrl)
                    .centerCrop()
                    .placeholder(R.drawable.sale_11)
                    .error(R.drawable.ic_error_image)
                    .into(this.root.memoThumb)
            } else {
//                이미지가 없으면 기본이미지 보여주기
                Glide.with(binding.root)
                    .load(R.drawable.sale_11)
                    .centerCrop()
                    .placeholder(R.drawable.sale_11)
                    .error(R.drawable.ic_error_image)
                    .into(this.root.memoThumb)
            }

            clickListener = View.OnClickListener {
//                클릭리스너를 이용해 현재 선택된 Memo의 Id를 알려줌
                listener.onClick(item.memoId)
            }
        }
    }
}

interface MemoItemClick {
    fun onClick(id: Int)
}