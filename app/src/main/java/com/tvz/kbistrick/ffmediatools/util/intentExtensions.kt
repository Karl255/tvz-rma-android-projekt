package com.tvz.kbistrick.ffmediatools.util

import android.content.Intent

fun Intent.tryGetIntExtra(name: String): Int? {
    return if (hasExtra(name)) {
        getIntExtra(name, 0)
    } else {
        null
    }
}