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
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.programmers.android.apps.line.PACKAGE_NAME
import com.programmers.android.apps.line.R
import com.programmers.android.apps.line.adapters.MemoImagesAdapter
import com.programmers.android.apps.line.adapters.viewholders.ImageClickListener
import com.programmers.android.apps.line.models.ArrayListLiveData
import com.programmers.android.apps.line.extensions.createFile
import com.programmers.android.apps.line.extensions.loge
import com.programmers.android.apps.line.models.Memo
import com.programmers.android.apps.line.models.MemoImage
import com.programmers.android.apps.line.ui.views.ImageAddDialog
import com.programmers.android.apps.line.ui.views.ImageViewDialog
import com.programmers.android.apps.line.viewmodels.MemoDetailViewModel
import com.programmers.android.apps.line.viewmodels.MemoListViewModel
import kotlinx.android.synthetic.main.activity_memo_detail.*
import kotlinx.android.synthetic.main.activity_memo_detail.toolbar
import kotlinx.android.synthetic.main.dialog_image_add.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.IOException

class MemoDetatilActivity : AppCompatActivity(), View.OnClickListener {
    private val IMAGE_FROM_GALLERY = 101
    private val IMAGE_FROM_CAMERA = 103

    private val ioDispather = Dispatchers.IO
    private lateinit var memosListViewModel: MemoListViewModel
    private lateinit var memoDetailViewModel: MemoDetailViewModel

    private lateinit var memoImagesAdapter: MemoImagesAdapter

    private lateinit var memo: Memo

    private var imageAddDialog: ImageAddDialog? = null

    private enum class Mode {
        WRITE, READ, MODIFY
    }

    private var mode = Mode.WRITE
    private val isRead: Boolean
        get() = mode == Mode.READ

    private var photoUri: Uri? = null
    private val livedataImages =
        ArrayListLiveData<MemoImage?>()

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_detail, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_delete -> {
                memosListViewModel.deleteMemo(memo)
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

        memoImagesAdapter = MemoImagesAdapter(this, imageClickListener)

        memosListViewModel = ViewModelProvider(this).get(MemoListViewModel::class.java)
        memoDetailViewModel = ViewModelProvider(this).get(MemoDetailViewModel::class.java)

        memoImagesRecyclerView.apply {
            layoutManager =
                LinearLayoutManager(context).apply { orientation = LinearLayoutManager.HORIZONTAL }
            adapter = memoImagesAdapter
        }

        livedataImages.observe(this, Observer { imageList ->
            memoImagesAdapter.images = imageList
            memoImagesAdapter.notifyDataSetChanged()
        })

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
                            memoDetailDescription.text.toString(),
                            livedataImages.value?.toList() ?: emptyList()
                        )
                        memosListViewModel.insertMemo(memo)
                    }
                    Mode.MODIFY -> {
                        memo.apply {
                            memoTitle = memoDetailTitle.text.toString()
                            memoDescription = memoDetailDescription.text.toString()
                            memoImages = livedataImages.value!!.toList()
                        }
                        memosListViewModel.modifyMemo(memo)
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
            memo = memosListViewModel.getMemo(receivedId)
            withContext(Dispatchers.Main) {
                btnMemoImageAdd.visibility = View.GONE
                tvMemoImageTitleHint.visibility = View.GONE
                memoDetailTitle.apply {
                    isEnabled = false
                    if (memo.memoTitle.isEmpty()) setText("")
                    else setText(memo.memoTitle)
                    setTextColor(Color.BLACK)
                }

                memoDetailDescription.apply {
                    isEnabled = false
                    if (memo.memoDescription.isEmpty()) setText("")
                    else setText(memo.memoDescription)
                    setTextColor(Color.BLACK)
                }

                if (memo.memoImages.isNotEmpty()) {
                    memo.memoImages.forEach { imagePath ->
                        imagePath?.deletable = isRead
                        livedataImages.add(imagePath)
                    }
                }
            }
        }
    }

    private fun setModifyMode() {
        mode = Mode.MODIFY
        loge("$isRead")
        memo.memoImages.forEach { it?.deletable = isRead }
        appBar.visibility = View.GONE
        btnMemoImageAdd.visibility = View.VISIBLE
        memoDetailTitle.isEnabled = true
        memoDetailDescription.isEnabled = true
    }

    private val dialogClickListener = object : ImageAddDialog.ImageAddDialogClickListener {
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
                            livedataImages.add(MemoImage(imageAddDialog?.btnWriteUrl?.text!!.toString()))
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
                    photoUri?.let { livedataImages.add(MemoImage(it.toString())) }
                }
                IMAGE_FROM_GALLERY -> {
                    data?.data?.let { livedataImages.add(MemoImage(it.toString())) }
                }
            }
        }
    }

    private val imageClickListener = object : ImageClickListener {
        override fun onView(position: Int) {
            loge("${livedataImages.value?.get(position)?.imageUrl}")
            ImageViewDialog.Builder(
                this@MemoDetatilActivity, livedataImages.value?.get(position)?.imageUrl
            ).show()
        }

        override fun onDelete(position: Int) {
            livedataImages.removeAt(position)
        }
    }


}