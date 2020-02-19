package com.programmers.android.apps.line.ui

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.programmers.android.apps.line.PACKAGE_NAME
import com.programmers.android.apps.line.R
import com.programmers.android.apps.line.adapters.MemoImagesAdapter
import com.programmers.android.apps.line.adapters.viewholders.ImageDeleteClickListener
import com.programmers.android.apps.line.extensions.createFile
import com.programmers.android.apps.line.extensions.loge
import com.programmers.android.apps.line.models.Memo
import com.programmers.android.apps.line.ui.views.ImageAddDialog
import com.programmers.android.apps.line.viewmodels.MemoImagesViewModel
import com.programmers.android.apps.line.viewmodels.MemoViewModel
import kotlinx.android.synthetic.main.activity_memo_detail.*
import kotlinx.android.synthetic.main.activity_memo_detail.toolbar
import kotlinx.android.synthetic.main.dialog_image_add.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException

class MemoDetatilActivity : AppCompatActivity(), View.OnClickListener {
    private val IMAGE_FROM_GALLERY = 101
    private val IMAGE_FROM_CAMERA = 103

    private lateinit var memosViewModel: MemoViewModel
    private lateinit var memoImagesAdapter: MemoImagesAdapter

    private lateinit var memoImagesViewModel: MemoImagesViewModel

    //    private var memo: Memo? = null
    private lateinit var memo: Memo

    private var imageAddDialog: ImageAddDialog? = null

    private enum class Mode {
        WRITE, READ, MODIFY
    }

    private var mode = Mode.WRITE
    private var photoUri: Uri? = null
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_detail, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_delete -> {
                memo.let {
                    memosViewModel.deleteMemo(it)
                }
                finish()
                true
            }
            R.id.action_modify -> {
                setModifyMode()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_memo_detail)
        setSupportActionBar(toolbar)

        memoImagesAdapter = MemoImagesAdapter(this, imageDeleteClickListener)
        memosViewModel = ViewModelProvider(this).get(MemoViewModel::class.java)

        memoImagesViewModel = ViewModelProvider(this).get(MemoImagesViewModel::class.java)

        memoImagesRecyclerView.apply {
            layoutManager =
                LinearLayoutManager(context).apply { orientation = LinearLayoutManager.HORIZONTAL }
            adapter = memoImagesAdapter
        }

        val receivedId = intent.getIntExtra("id", -1)
        if (receivedId != -1) {
            setReadMode(receivedId)
        }
        btnMemoCancel.setOnClickListener(this)
        btnMemoOk.setOnClickListener(this)
        btnMemoImageAdd.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when (view) {
            btnMemoCancel -> finish()
            btnMemoOk -> {
                when (mode) {
                    Mode.WRITE -> {
                        val memo = Memo(
                            memoDetailTitle.text.toString(),
                            memoDetailText.text.toString(),
                            memoImagesAdapter.images
                        )
                        memosViewModel.insertMemo(memo)
                    }
                    Mode.MODIFY -> {
                        memo.apply {
                            memoTitle = memoDetailTitle.text.toString()
                            memoText = memoDetailText.text.toString()
                            memoImages = memoImagesAdapter.images
                        }
                        memosViewModel.modifyMemo(memo)
                    }
                    Mode.READ -> {
                    }
                }
                finish()
            }
            btnMemoImageAdd -> {
                imageAddDialog = ImageAddDialog.Builder(this, dialogClickListener).show()
            }
        }
    }

    private fun setReadMode(receivedId: Int) {
        mode = Mode.READ
        appBar.visibility = View.VISIBLE
        CoroutineScope(Dispatchers.IO).launch {
            memo = memosViewModel.getMemo(receivedId)

            btnMemoImageAdd.visibility = View.GONE
            tvMemoImageTitleHint.visibility = View.GONE
            memoDetailTitle.apply {
                isEnabled = false
                if (memo.memoTitle.isEmpty()) setText("")
                else setText(memo.memoTitle)
                setTextColor(Color.BLACK)
            }

            memoDetailText.apply {
                isEnabled = false
                if (memo.memoText.isEmpty()) setText("")
                else setText(memo.memoText)
                setTextColor(Color.BLACK)
            }

            if (memo.memoImages.isNotEmpty()) {
                memo.memoImages.forEach { imagePath ->
                    memoImagesAdapter.images.add(imagePath)
                }
                memoImagesAdapter.notifyDataSetChanged()
            }
        }
    }

    private fun setModifyMode() {
        mode = Mode.MODIFY
        appBar.visibility = View.GONE
        btnMemoImageAdd.visibility = View.VISIBLE

        memoDetailTitle.isEnabled = true
        memoDetailText.isEnabled = true
    }

    private val dialogClickListener = object : ImageAddDialog.DialogClickListener {
        override fun onclick(id: Int?) {
            when (id) {
                R.id.btnTakePicture -> {
                    Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { intent ->
                        intent.resolveActivity(packageManager)?.also {
                            val photoFile: File? = try {
                                createFile()
                            } catch (e: IOException) {
                                null
                            }

                            photoFile?.also {
                                photoUri = FileProvider.getUriForFile(
                                    this@MemoDetatilActivity,
                                    PACKAGE_NAME,
                                    it
                                )
                                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
                                startActivityForResult(intent, IMAGE_FROM_CAMERA)
                            }
                        }
                    }
                }

                R.id.btnPickGallery -> {
                    startActivityForResult(
                        Intent.createChooser(
                            Intent(Intent.ACTION_OPEN_DOCUMENT)
                                .setType("image/*")
                            , getString(R.string.memo_image_add_hint)
                        ), IMAGE_FROM_GALLERY
                    )
                }

                R.id.btnDialogOk -> {
                    imageAddDialog?.let {
                        if (imageAddDialog?.btnWriteUrl?.text!!.isNotEmpty()) {
                            memoImagesAdapter.images.add(imageAddDialog?.btnWriteUrl?.text?.toString())
                        }
                    }
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                IMAGE_FROM_CAMERA -> {
                    photoUri?.let {
                        memoImagesAdapter.apply {
                            images.add(it.toString())
                            notifyItemInserted(itemCount)
                        }
                    }
                }
                IMAGE_FROM_GALLERY -> {
                    data?.data?.let {
                        memoImagesAdapter.apply {
                            images.add(it.toString())
                            notifyItemInserted(itemCount)
                        }
                    }
                }
            }
        }
    }

    private val imageDeleteClickListener = object : ImageDeleteClickListener {
        override fun onClick(position: Int) {
            memoImagesAdapter.images.removeAt(position)
            memoImagesAdapter.notifyItemRemoved(position)
            memoImagesAdapter.notifyDataSetChanged()
        }
    }
}