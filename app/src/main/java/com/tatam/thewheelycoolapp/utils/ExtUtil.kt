package com.tatam.thewheelycoolapp.utils

import android.content.Context
import android.widget.Toast

fun Context.showToastRes(message : Int) {
    Toast.makeText(this,  message, Toast.LENGTH_SHORT).show()
}