package com.programmers.android.apps.line.ui.views

import android.app.Dialog
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import com.programmers.android.apps.line.R
import kotlinx.android.synthetic.main.dialog_image_pick.*

class ImagePickDialog(context: Context, private val listener: DialogClickListener): Dialog(context), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_image_pick)
        setCanceledOnTouchOutside(false)

        window?.apply {
            setBackgroundDrawable(ColorDrawable())
            setDimAmount(0.9f)
        }

        btnTakePicture.setOnClickListener(this)
        btnPickGallery.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        listener.onclick(view?.id)
        this.dismiss()
    }

    class Builder(context: Context, listener: DialogClickListener) {
        private val dialog = ImagePickDialog(context, listener)

        fun show(): ImagePickDialog {
            dialog.show()
            return dialog
        }
    }

    interface DialogClickListener {
        fun onclick(id: Int?)
    }
}