package com.raqun.permylib

import android.os.Build

class OsUtil {

    companion object {
        fun hasM() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
    }
}