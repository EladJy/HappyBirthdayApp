package com.ej.happybirthdayapp.utils

import android.content.Context
import androidx.annotation.StringRes
import javax.inject.Inject

class StringsMapper @Inject constructor(context: Context) {
    
    private val resources = context.resources

    fun getString(@StringRes resId: Int): String = resources.getString(resId)

    fun getString(@StringRes resId: Int, vararg formatArgs: Any): String =
        resources.getString(resId, *formatArgs)

}