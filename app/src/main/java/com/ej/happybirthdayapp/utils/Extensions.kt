package com.ej.happybirthdayapp.utils

import android.graphics.Bitmap
import android.graphics.Canvas
import android.view.View
import org.joda.time.DateTime
import org.joda.time.Months
import org.joda.time.Years

class Extensions {}fun DateTime.getYearsFromNow(): Int {
    return Years.yearsBetween(this, DateTime()).years
}

fun DateTime.getMonthsFromNow(): Int {
    return Months.monthsBetween(this, DateTime()).months
}

fun View.getBitmapFromView(): Bitmap? {
    var bitmap =
        Bitmap.createBitmap(this.width, this.height, Bitmap.Config.ARGB_8888)
    var canvas = Canvas(bitmap)
    this.draw(canvas)
    return bitmap
}