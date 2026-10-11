package com.maxdgf.newspaper.presentation.common.utils

import android.content.Context
import android.content.Intent

class ShareManager(private val _context: Context) {
    fun shareMessage(message: String): Boolean {
        val intent = Intent(Intent.ACTION_SEND).apply {
            putExtra(Intent.EXTRA_TEXT, message)
            type = "text/plain"
        }
        val intentChooser = Intent.createChooser(intent, null)

        return if (intentChooser.resolveActivity(_context.packageManager) != null) {
            _context.startActivity(intentChooser)
            true
        } else false
    }
}