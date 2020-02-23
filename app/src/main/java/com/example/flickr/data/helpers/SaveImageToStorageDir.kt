package com.example.flickr.data.helpers

import android.content.Context
import android.graphics.Bitmap
import android.icu.text.SimpleDateFormat
import android.os.Environment
import android.widget.Toast
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.util.*

object SaveImageToStorageDir {
    fun save(context: Context, image: Bitmap): String {
        var imagePath = ""
        val imageFileName = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date()) + ".jpg"
        context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)?.let { storageDir ->
            var success = true
            if (!storageDir.exists()) {
                success = storageDir.mkdirs()
            }
            if (success) {
                val imageFile = File(storageDir, imageFileName)
                imagePath = imageFile.getAbsolutePath()
                try {
                    val fOut: OutputStream = FileOutputStream(imageFile)
                    image.compress(Bitmap.CompressFormat.JPEG, 100, fOut)
                    fOut.close()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                Toast.makeText(context, "IMAGE SAVED", Toast.LENGTH_LONG).show()
            }
        }
        return imagePath;
    }
}
