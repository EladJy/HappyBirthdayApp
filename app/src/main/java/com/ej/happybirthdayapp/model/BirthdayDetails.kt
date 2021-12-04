package com.ej.happybirthdayapp.model

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class BirthdayDetails(val fullName: String, val birthdayTimestamp: Long, val imageUri: Uri? = null): Parcelable