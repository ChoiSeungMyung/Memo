package com.programmers.android.apps.line.utilities

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

/**
 * 권한 요청을 위한 object
 *
 * @property REQUIRED_ALL_PERMISSIONS : 카메라, 외부저장소 권한 - 앱에 필요한 모든 권한(2020. 02. 23)
 * @property REQUIRED_STORAGE_PERMISSION : 외부저장소 권한 - 외부저장소에 저장되어있는 사진만 가져오기 위한 권한
 * @property REQUEST_CODE_PERMISSIONS : 권한 요청 코드값
 *
 * - 2020. 02. 23 기준
 * - 카메라를 이용하기 위해선 카메라, 외부저장소 다 필요하므로 따로 카메라만 요청권한 없음
 */
object PermissionUtil {
    const val REQUEST_CODE_PERMISSIONS = 10
    val REQUIRED_ALL_PERMISSIONS = arrayOf(
        Manifest.permission.CAMERA,
        Manifest.permission.WRITE_EXTERNAL_STORAGE)
    val REQUIRED_STORAGE_PERMISSION = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)

    fun permissionsAllGranted(context: Context) = REQUIRED_ALL_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
    }

    fun permissionStorageGranted(context: Context) = REQUIRED_STORAGE_PERMISSION.all {
        ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
    }
}
