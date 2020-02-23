package com.programmers.android.apps.line.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.programmers.android.apps.line.R
import com.programmers.android.apps.line.adapters.viewholders.ImagesListViewHolder
import com.programmers.android.apps.line.viewmodels.MemoDetailViewModel

class MemoImagesAdapter(
    private val context: Context,
    private val viewModel: MemoDetailViewModel
) : ListAdapter<String ,ImagesListViewHolder>(MemoDiffCallBack()) {
    var images = arrayListOf<String?>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImagesListViewHolder =
        ImagesListViewHolder(
            context,
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.list_image_item,
                parent,
                false
            )
        )

    override fun getItemCount(): Int = images.size

    override fun onBindViewHolder(holder: ImagesListViewHolder, position: Int) {
        val imageUri = images[position]
        holder.bind(viewModel, imageUri, position)
    }
}

class MemoDiffCallBack: DiffUtil.ItemCallback<String>() {
    override fun areItemsTheSame(oldItem: String, newItem: String): Boolean = oldItem ==  newItem

    override fun areContentsTheSame(oldItem: String, newItem: String): Boolean = oldItem == newItem
}