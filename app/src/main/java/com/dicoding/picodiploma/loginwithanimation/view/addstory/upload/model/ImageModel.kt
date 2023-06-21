package com.dicoding.picodiploma.loginwithanimation.view.addstory.upload.model

import android.graphics.Bitmap
import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.io.File

@Parcelize
data class ImageModel(
    val imageFile: File,
    val imageUri: Uri? = null,
    val imageBitmap: Bitmap? = null,
    val isFromCamera: Boolean = true
) : Parcelable