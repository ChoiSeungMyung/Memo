package com.programmers.android.apps.line.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.programmers.android.apps.line.R
import com.programmers.android.apps.line.adapters.viewholders.ImageDeleteClickListener
import com.programmers.android.apps.line.adapters.viewholders.ImagesListViewHolder

class MemoImagesAdapter(
    private val context: Context,
    private val listener: ImageDeleteClickListener
) : RecyclerView.Adapter<ImagesListViewHolder>() {
    val images = arrayListOf<String?>()

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
        val imagePath = images[position]
        holder.bind(listener, imagePath, position)
    }
}