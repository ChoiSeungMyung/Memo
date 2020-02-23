package com.programmers.android.apps.line.adapters.viewholders

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.android.material.snackbar.Snackbar
import com.programmers.android.apps.line.R
import com.programmers.android.apps.line.databinding.ListImageItemBinding
import com.programmers.android.apps.line.extensions.showSnackBar
import com.programmers.android.apps.line.ui.views.ImageViewDialog
import com.programmers.android.apps.line.viewmodels.MemoDetailViewModel
import kotlinx.android.synthetic.main.list_image_item.view.*

class ImagesListViewHolder(
    private val context: Context,
    private val binding: ListImageItemBinding
) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(viewModel: MemoDetailViewModel, imageUri: String?, position: Int) {
        binding.viewModel = viewModel
        binding.apply {
            imageUri?.let {
                Glide.with(context)
                    .load(imageUri)
                    .centerCrop()
                    .listener(object : RequestListener<Drawable> {
                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Drawable>?,
                            isFirstResource: Boolean
                        ): Boolean {
                            try {
//                                에러 발생시 스낵바를 통해 사용자에게 알리고 viewModel에게는 데이터 삭제를 지시함
                                root.showSnackBar(context.resources.getString(R.string.toast_image_load_fail), Snackbar.LENGTH_SHORT)
                                viewModel.imageRemoveAt(position)
                            } catch (e: IllegalArgumentException) { }
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
                    .error(R.drawable.ic_error_image) // 예상치 못한 에러상황을 대비하기 위한 에러이미지
                    .into(binding.root.imageThumb)
            }

            deleteClickListener = View.OnClickListener {
                viewModel.imageRemoveAt(position)
            }

            viewClickListener = View.OnClickListener {
                ImageViewDialog.Builder(
                    it.context, imageUri
                ).show()
            }
        }
    }
}