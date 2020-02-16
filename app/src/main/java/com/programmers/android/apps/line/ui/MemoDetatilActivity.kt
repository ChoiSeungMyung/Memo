package com.programmers.android.apps.line.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.programmers.android.apps.line.R
import com.programmers.android.apps.line.extensions.loge
import com.programmers.android.apps.line.models.Memo
import com.programmers.android.apps.line.ui.views.ImagePickDialog
import com.programmers.android.apps.line.viewmodels.MemoViewModel
import kotlinx.android.synthetic.main.activity_memo_detail.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MemoDetatilActivity : AppCompatActivity(), View.OnClickListener {
    private val PICK_FROM_GALLERY = 100
    private lateinit var memosViewModel: MemoViewModel
    private val images = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_memo_detail)
        btnMemoCancel.setOnClickListener(this)
        btnMemoOk.setOnClickListener(this)
        btnMemoImageAdd.setOnClickListener(this)

        memosViewModel = ViewModelProvider(this).get(MemoViewModel::class.java)
    }

    override fun onClick(view: View?) {
        when (view) {
            btnMemoCancel -> finish()
            btnMemoOk -> {
                val memo = Memo(memoDetailTitle.text.toString(), memoDetailText.text.toString())
                CoroutineScope(Dispatchers.IO).launch { memosViewModel.insertMemo(memo) }
                finish()
            }
            btnMemoImageAdd -> {
                ImagePickDialog.Builder(this, dialogClickListener).show()
            }
        }
    }

    private val dialogClickListener = object : ImagePickDialog.DialogClickListener {
        override fun onclick(id: Int?) {
            when(id) {
                R.id.btnTakePicture -> {
                    loge("click take picture")
                }

                R.id.btnPickGallery -> {
                    startActivityForResult(
                        Intent.createChooser(
                            Intent(Intent.ACTION_GET_CONTENT)
                                .setType("image/*")
                        ,getString(R.string.memo_image_add_hint)
                        ), PICK_FROM_GALLERY
                    )
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode) {
            100 -> {
                val selectedImage = data?.data
                Glide.with(this)
                    .load(selectedImage)
                    .centerCrop()
                    .into(testImage)
            }
        }
        loge("$requestCode, $resultCode, ${data}")
    }
}