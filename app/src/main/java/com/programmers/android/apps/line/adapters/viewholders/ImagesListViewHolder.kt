package com.programmers.android.apps.line.adapters.viewholders

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.android.material.snackbar.Snackbar
import com.programmers.android.apps.line.R
import com.programmers.android.apps.line.databinding.ListImageItemBinding
import com.programmers.android.apps.line.extensions.loge
import com.programmers.android.apps.line.models.MemoImage
import kotlinx.android.synthetic.main.list_image_item.view.*

class ImagesListViewHolder(
    private val context: Context,
    private val binding: ListImageItemBinding
) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(listener: ImageClickListener, memoImage: MemoImage?, position: Int) {
        binding.memoImage = memoImage
        loge("${memoImage?.deletable}")
        binding.apply {
            memoImage?.let {
                Glide.with(context)
                    .load(memoImage.imageUrl)
                    .centerCrop()
                    .listener(object : RequestListener<Drawable> {
                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Drawable>?,
                            isFirstResource: Boolean
                        ): Boolean {
                            try {
                                Snackbar.make(
                                    binding.root,
                                    context.resources.getString(R.string.toast_image_load_fail),
                                    Snackbar.LENGTH_SHORT
                                ).show()
                            } catch (e: IllegalArgumentException) {
                                Toast.makeText(context, context.resources.getString(R.string.toast_glide_error), Toast.LENGTH_LONG).show()
                            }
                            return false
                        }

                        override fun onResourceReady(
                            resource: Drawable?,
                            model: Any?,
                            target: Target<Drawable>?,
                            dataSource: DataSource?,
                            isFirstResource: Boolean
                        ): Boolean = false
                    })
                    .error(R.drawable.ic_camera_undo)
                    .into(binding.root.imageThumb)
            }

            deleteClickListener = View.OnClickListener {
                listener.onDelete(position)
            }

            viewClickListener = View.OnClickListener {
                listener.onView(position)
            }
        }
    }
}

interface ImageClickListener {
    fun onView(position: Int)
    fun onDelete(position: Int)
}