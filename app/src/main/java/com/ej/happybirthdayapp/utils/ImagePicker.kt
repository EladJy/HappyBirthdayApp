package com.ej.happybirthdayapp.utils

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.Parcelable
import android.provider.MediaStore
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import javax.inject.Inject

class ImagePicker @Inject constructor(private val context: Context) {

    private var cameraImageFile: File? = null
    var imageUri: Uri? = null

    fun getPickIntent(): Intent? {
        val intents: ArrayList<Intent> = arrayListOf()
        intents.add(Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI))
        setCameraIntents(intents)
        if (intents.isEmpty()) return null
        val result = Intent.createChooser(intents.removeAt(0), null)
        if (intents.isNotEmpty()) {
            result.putExtra(Intent.EXTRA_INITIAL_INTENTS, intents.toArray(arrayOf<Parcelable>()))
        }
        return result
    }

    private fun setCameraIntents(cameraIntents: MutableList<Intent>) {
        cameraImageFile = null
        val tempFile = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
            System.currentTimeMillis().toString() + ".jpeg")
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.flags = Intent.FLAG_GRANT_WRITE_URI_PERMISSION
            imageUri = FileProvider.getUriForFile(context, context.packageName, tempFile)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
        } else {
            imageUri = Uri.fromFile(tempFile)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
        }
        cameraImageFile = tempFile
        cameraIntents.add(intent)
    }

    fun saveImage(image: Bitmap?): Uri? {
        val file = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
            System.currentTimeMillis().toString() + ".jpeg")
        var uri: Uri? = null
        try {
            val stream = FileOutputStream(file)
            image?.compress(Bitmap.CompressFormat.PNG, 90, stream)
            stream.flush()
            stream.close()
            uri = FileProvider.getUriForFile(context, context.packageName, file)
        } catch (e: IOException) {
        }
        return uri
    }
}