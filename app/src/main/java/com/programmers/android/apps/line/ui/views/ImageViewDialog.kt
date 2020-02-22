package com.programmers.android.apps.line.ui.views

import android.app.Dialog
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.programmers.android.apps.line.R
import kotlinx.android.synthetic.main.dialog_image_view.*

class ImageViewDialog(context: Context, private val imageUri: String?) : Dialog(context),
    View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_image_view)
        setCanceledOnTouchOutside(true)

        window?.apply {
            setBackgroundDrawable(ColorDrawable())
            setDimAmount(0.9f)
        }

        imageUri?.let {
            Glide.with(context)
                .load(imageUri)
                .into(imageView)
        }
    }

    override fun onClick(p0: View?) {
        this.dismiss()
    }

    class Builder(context: Context, imageUri: String?) {
        private val dialog = ImageViewDialog(context, imageUri)

        fun show(): ImageViewDialog {
            dialog.show()
            return dialog
        }
    }
}