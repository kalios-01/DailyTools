package com.kalios.roomdbdemo // replace with your package name

import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import android.graphics.BitmapFactory
import android.util.Log
import java.io.ByteArrayOutputStream

object BitmapUtils {

    private const val TAG = "BitmapUtils"

    fun bitmapToByteArray(bitmap: Bitmap?): ByteArray? {
        if (bitmap == null) {
            Log.e(TAG, "Input bitmap is null")
            return null
        }
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(CompressFormat.PNG, 100, byteArrayOutputStream)
        return byteArrayOutputStream.toByteArray()
    }

    fun byteArrayToBitmap(byteArray: ByteArray?): Bitmap? {
        if (byteArray == null) {
            Log.e(TAG, "Input byte array is null")
            return null
        }
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
    }
}