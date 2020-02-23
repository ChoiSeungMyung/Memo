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
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.programmers.android.apps.line.PACKAGE_NAME
import com.programmers.android.apps.line.R
import com.programmers.android.apps.line.databinding.ActivityMemoDetailBinding
import com.programmers.android.apps.line.extensions.createFile
import com.programmers.android.apps.line.ui.views.ImageAddDialog
import com.programmers.android.apps.line.utilities.PermissionUtil
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

    /**
     * Activity가 생성 될 때 intent로 전달된 id가 있다면 읽기 모드로 전환
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(toolbar)
        btnMemoImageAdd.setOnClickListener(this)
        val binding: ActivityMemoDetailBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_memo_detail)

        memoDetailViewModel = ViewModelProvider(this).get(MemoDetailViewModel::class.java)
        binding.detailViewmodel = memoDetailViewModel

        memoImagesRecyclerView.adapter = memoDetailViewModel.memoImagesAdapter

        memoDetailViewModel.images.observe(this, Observer { imageList ->
            memoDetailViewModel.memoImagesAdapter.images = imageList
            memoDetailViewModel.memoImagesAdapter.notifyDataSetChanged()
        })

        intent.getStringExtra("id")?.let {
            if (!memoDetailViewModel.hasInit) {
                memoDetailTitle.clearFocus()
                memoDetailDescription.clearFocus()
                memoDetailViewModel.setReadMode(it)
            }
        }
    }

    override fun onClick(view: View?) {
        when (view) {
            btnMemoImageAdd -> imageAddDialog =
                ImageAddDialog.Builder(this, dialogClickListener).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                IMAGE_FROM_CAMERA -> photoUri?.let { memoDetailViewModel.images.add(it.toString()) }

                IMAGE_FROM_GALLERY -> data?.data?.let { memoDetailViewModel.images.add(it.toString()) }
            }
        }
    }

    private fun getPermission(permissions: Array<String>) {
        ActivityCompat.requestPermissions(
            this,
            permissions,
            PermissionUtil.REQUEST_CODE_PERMISSIONS
        )
    }

    /**
     * 다이얼로그에서 어떤 이벤트가 발생햇는지에 따라 다른 동작을 함
     *
     * 카메라, 외부저장소 접근 권한이 필요하므로 권한체크 추가
     */
    private val dialogClickListener = object : ImageAddDialog.ImageAddDialogClickListener {
        override fun onclick(id: Int?) {
            when (id) {
                R.id.btnTakePicture -> {
                    when (PermissionUtil.permissionsAllGranted(this@MemoDetailActivity)) {
                        true -> { // 권한 승인 -> 카메라로 찍은 사진 가져오기
                            Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { intent ->
                                intent.resolveActivity(packageManager)?.also {
                                    val photoFile: File? = try {
                                        createFile()
                                    } catch (e: IOException) { null }

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
                        false -> getPermission(PermissionUtil.REQUIRED_ALL_PERMISSIONS) // 권한 거절 -> 권한 요청
                    }
                }

                R.id.btnPickGallery -> {
                    when (PermissionUtil.permissionStorageGranted(this@MemoDetailActivity)) {
                        true -> { // 권한승인 -> 외부저장소에서 사진 가져오기, Intent.ACTION_OPEN_DOCUMENT 플래그를 이용해 지속 사용 가능한 uri 추출
                            val intent =
                                Intent(Intent.ACTION_OPEN_DOCUMENT).apply { type = "image/*" }
                            startActivityForResult(
                                Intent.createChooser(
                                    intent, getString(R.string.memo_image_add_hint)
                                ), IMAGE_FROM_GALLERY
                            )
                        }

                        false -> getPermission(PermissionUtil.REQUIRED_STORAGE_PERMISSION)
                    }
                }

                /*
                 * url입력란이 비어있지 않다면 url입력으로 판단
                 */
                R.id.btnDialogOk -> {
                    imageAddDialog?.let {
                        if (imageAddDialog?.btnWriteUrl?.text!!.isNotEmpty()) {
                            memoDetailViewModel.images.add(imageAddDialog?.btnWriteUrl?.text!!.toString())
                        }
                    }
                }
            }
        }
    }
}