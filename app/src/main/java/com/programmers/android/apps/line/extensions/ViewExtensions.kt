package com.programmers.android.apps.line.extensions

import android.view.View
import com.google.android.material.snackbar.Snackbar

fun View.showSnackBar(msg: String, duration: Int) = Snackbar.make(this, msg, duration).show()