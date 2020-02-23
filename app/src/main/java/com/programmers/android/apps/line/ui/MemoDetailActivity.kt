package com.programmers.android.apps.line.ui

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.programmers.android.apps.line.PACKAGE_NAME
import com.programmers.android.apps.line.R
import com.programmers.android.apps.line.databinding.ActivityMemoDetailBinding
import com.programmers.android.apps.line.extensions.createFile
import com.programmers.android.apps.line.models.MemoImage
import com.programmers.android.apps.line.ui.views.ImageAddDialog
import com.programmers.android.apps.line.viewmodels.MemoDetailViewModel
import kotlinx.android.synthetic.main.activity_memo_detail.*
import kotlinx.android.synthetic.main.dialog_image_add.*
import java.io.File
import java.io.IOException

class MemoDetailActivity : AppCompatActivity(), View.OnClickListener {
    private val IMAGE_FROM_GALLERY = 101
    private val IMAGE_FROM_CAMERA = 103

    private lateinit var memoDetailViewModel: MemoDetailViewModel

    private var imageAddDialog: ImageAddDialog? = null

    private var photoUri: Uri? = null

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_detail, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_delete -> {
                memoDetailViewModel.deleteMemo()
                finish()
                true
            }
            R.id.action_modify -> {
                memoDetailViewModel.setModifyMode()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityMemoDetailBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_memo_detail)
        setSupportActionBar(toolbar)

        memoDetailViewModel = ViewModelProvider(this).get(MemoDetailViewModel::class.java)
        binding.detailViewmodel = memoDetailViewModel

        memoImagesRecyclerView.apply { adapter = memoDetailViewModel.memoImagesAdapter }

        memoDetailViewModel.images.observe(this, Observer { imageList ->
            memoDetailViewModel.memoImagesAdapter.images = imageList
            memoDetailViewModel.memoImagesAdapter.notifyDataSetChanged()
        })

        val receivedId = intent.getIntExtra("id", -1)
        if (receivedId != -1 && !memoDetailViewModel.hasInit) {
            memoDetailViewModel.setReadMode(receivedId)
        }

        btnMemoImageAdd.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when (view) {
            btnMemoImageAdd -> imageAddDialog = ImageAddDialog.Builder(this, dialogClickListener).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                IMAGE_FROM_CAMERA -> photoUri?.let { memoDetailViewModel.images.add(MemoImage(it.toString())) }

                IMAGE_FROM_GALLERY -> data?.data?.let { memoDetailViewModel.images.add(MemoImage(it.toString())) }
            }
        }
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
                                    this@MemoDetailActivity,
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
                            memoDetailViewModel.images.add(MemoImage(imageAddDialog?.btnWriteUrl?.text!!.toString()))
                        }
                    }
                }
            }
        }
    }
}