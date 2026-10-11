package com.maxdgf.newspaper.presentation.common.utils

import android.content.Context
import android.content.Intent
import androidx.core.net.toUri

class UrlOpener(private val _context: Context) {
    fun openUrlInBrowser(url: String): Boolean {
        val intent = Intent(Intent.ACTION_VIEW, url.toUri())

        return if (intent.resolveActivity(_context.packageManager) != null) {
            _context.startActivity(intent)
            true
        } else false
    }
}