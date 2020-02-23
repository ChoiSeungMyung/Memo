package com.programmers.android.apps.line.ui.views

import android.app.Dialog
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import com.programmers.android.apps.line.R
import kotlinx.android.synthetic.main.dialog_image_add.*

class ImageAddDialog(context: Context, private val listener: ImageAddDialogClickListener): Dialog(context), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_image_add)
        setCanceledOnTouchOutside(false)

        window?.apply {
            setBackgroundDrawable(ColorDrawable())
            setDimAmount(0.9f)
        }

        btnTakePicture.setOnClickListener(this)
        btnPickGallery.setOnClickListener(this)
        btnDialogOk.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        listener.onclick(view?.id)
        this.dismiss()
    }

    class Builder(context: Context, listenerImageAdd: ImageAddDialogClickListener) {
        private val dialog = ImageAddDialog(context, listenerImageAdd)

        fun show(): ImageAddDialog {
            dialog.show()
            return dialog
        }
    }

    interface ImageAddDialogClickListener {
        fun onclick(id: Int?)
    }
}