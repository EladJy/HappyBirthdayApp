package com.ej.happybirthdayapp.ui.fragments.birthday

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import com.ej.happybirthdayapp.R
import com.ej.happybirthdayapp.model.*
import com.ej.happybirthdayapp.ui.base.BasePresenter
import com.ej.happybirthdayapp.ui.base.BaseView
import com.ej.happybirthdayapp.utils.ImagePicker
import com.ej.happybirthdayapp.utils.StringsMapper
import com.ej.happybirthdayapp.utils.getMonthsFromNow
import com.ej.happybirthdayapp.utils.getYearsFromNow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.joda.time.DateTime
import javax.inject.Inject

interface BirthdayMvpView : BaseView {
    fun setBirthdayTitle(birthdayTitle: String?)
    fun setBirthdaySubtitle(subTitle: String)
    fun setBirthdayAge(age: Int)
    fun setCroppedImage(imageUri: Uri?)
    fun showImagePicker(intent: Intent?)
    fun shareImage(image: Uri?)
    fun setScreenElements(elements: BirthdayScreenElements?)

}

class BirthdayPresenter @Inject constructor(private val imagePicker: ImagePicker,
                                            private val stringsMapper: StringsMapper) :
    BasePresenter<BirthdayMvpView>() {

    private var birthdayDetails: BirthdayDetails? = null

    fun setArguments(birthdayDetails: BirthdayDetails?) {
        this.birthdayDetails = birthdayDetails
        val birthdayTitle = birthdayDetails?.fullName?.let {
            stringsMapper.getString(R.string.birthday_name_title, it)
        }
        setScreenElements()
        mvpView?.setBirthdayTitle(birthdayTitle)
        calculateDiffFromBirthday()
        mvpView?.setCroppedImage(birthdayDetails?.imageUri)
    }


    private fun setScreenElements() {
        val elements = when (birthdayDetails?.birthdayScreenStyle) {
            BirthdayScreenStyle.FOX -> BirthdayScreenElements(R.drawable.i_os_bg_fox,
                R.color.blue_bg,
                R.drawable.default_place_holder_green,
                R.drawable.camera_icon_green,
                R.drawable.circle_border_green)
            BirthdayScreenStyle.ELEPHANT -> BirthdayScreenElements(R.drawable.i_os_bg_elephant,
                R.color.yellow_bg,
                R.drawable.default_place_holder_yellow,
                R.drawable.camera_icon_yellow,
                R.drawable.circle_border_yellow)
            BirthdayScreenStyle.PELICAN -> BirthdayScreenElements(R.drawable.i_os_bg_pelican_2,
                R.color.blue_bg,
                R.drawable.default_place_holder_blue,
                R.drawable.camera_icon_blue,
                R.drawable.circle_border_blue)
            else -> null
        }
        mvpView?.setScreenElements(elements)
    }

    private fun calculateDiffFromBirthday() {
        val birthdayDateTime = DateTime(birthdayDetails?.birthdayTimestamp)
        val birthdayAge = when {
            birthdayDateTime.getYearsFromNow() > 0 -> BirthdayAge(birthdayDateTime.getYearsFromNow(),
                TimeType.YEARS)
            else -> BirthdayAge(birthdayDateTime.getMonthsFromNow(), TimeType.MONTHS)
        }
        val subTitle = getSubtitleByAge(birthdayAge)
        mvpView?.setBirthdaySubtitle(subTitle)
        mvpView?.setBirthdayAge(birthdayAge.age)
    }

    private fun getSubtitleByAge(birthdayAge: BirthdayAge): String {
        return when {
            birthdayAge.age > 1 && birthdayAge.timeType == TimeType.MONTHS -> stringsMapper.getString(
                R.string.birthday_age_months)
            birthdayAge.age in 0..1 && birthdayAge.timeType == TimeType.MONTHS -> stringsMapper.getString(
                R.string.birthday_age_month)
            birthdayAge.age > 1 && birthdayAge.timeType == TimeType.YEARS -> stringsMapper.getString(
                R.string.birthday_age_years)
            birthdayAge.age in 0..1 && birthdayAge.timeType == TimeType.YEARS -> stringsMapper.getString(
                R.string.birthday_age_year)
            else -> ""
        }
    }

    fun birthdayCameraIconClicked() {
        val intent = imagePicker.getPickIntent()
        mvpView?.showImagePicker(intent)
    }

    fun birthdayShareBtnClicked(bitmapFromView: Bitmap?) {
        scope.launch {
            val image = saveImage(bitmapFromView)
            mvpView?.shareImage(image)
        }
    }

    private suspend fun saveImage(bitmapFromView: Bitmap?) = withContext(Dispatchers.IO) {
        imagePicker.saveImage(bitmapFromView)
    }


    fun imageFromPickerArrived(imageUri: Uri?) {
        if (imageUri != null) {
            imagePicker.imageUri = imageUri
        }
        imagePicker.imageUri?.let {
            mvpView?.setCroppedImage(imagePicker.imageUri)
        }
    }

    fun removeImageUri() {
        imagePicker.imageUri = null
    }
}