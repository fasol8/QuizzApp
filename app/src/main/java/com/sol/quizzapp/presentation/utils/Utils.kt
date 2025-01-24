package com.sol.quizzapp.presentation.utils

import android.os.Build
import android.text.Html

fun decodeHtmlEntities(input: String): String {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        Html.fromHtml(input, Html.FROM_HTML_MODE_LEGACY).toString()
    } else {
        Html.fromHtml(input).toString()
    }
}