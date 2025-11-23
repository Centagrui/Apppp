package com.example.inventory.utils

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import java.io.File
import java.io.FileOutputStream

object FileManager {

    fun saveImage(context: Context, bitmap: Bitmap): Uri {
        val file = File(context.filesDir, "IMG_${System.currentTimeMillis()}.jpg")
        FileOutputStream(file).use { out ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
        }
        return Uri.fromFile(file)
    }

    fun saveTempFile(context: Context, prefix: String, ext: String = "tmp"): Uri {
        val file = File(context.filesDir, "${prefix}_${System.currentTimeMillis()}.$ext")
        return Uri.fromFile(file)
    }

    fun deleteFile(uri: String) {
        val file = File(uri)
        if (file.exists()) file.delete()
    }
}
