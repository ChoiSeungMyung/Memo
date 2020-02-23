package com.programmers.android.apps.line.extensions

import android.app.Activity
import android.os.Environment
import java.io.File
import java.io.IOException

@Throws(IOException::class)
fun Activity.createFile(): File? {
    val fileName = "line_${System.currentTimeMillis()}"
    val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)

    return File.createTempFile(fileName, ".jpg", storageDir)
}