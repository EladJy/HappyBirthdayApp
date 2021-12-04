package com.ej.happybirthdayapp.ui.fragments.birthday

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import com.ej.happybirthdayapp.R
import com.ej.happybirthdayapp.model.BirthdayAge
import com.ej.happybirthdayapp.model.BirthdayDetails
import com.ej.happybirthdayapp.model.TimeType
import com.ej.happybirthdayapp.ui.base.BasePresenter
import com.ej.happybirthdayapp.ui.base.BaseView
import com.ej.happybirthdayapp.utils.ImagePicker
import com.ej.happybirthdayapp.utils.StringsMapper
import com.ej.happybirthdayapp.utils.getMonthsFromNow
import com.ej.happybirthdayapp.utils.getYearsFromNow
import org.joda.time.DateTime
import javax.inject.Inject

interface BirthdayMvpView : BaseView {
    fun setBirthdayTitle(birthdayTitle: String?)
    fun setBirthdaySubtitle(subTitle: String)
    fun setBirthdayAge(age: Int)
    fun setCroppedImage(imageUri: Uri?)
    fun showImagePicker(intent: Intent?)
    fun shareImage(image: Uri?)

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
        mvpView?.setBirthdayTitle(birthdayTitle)
        calculateDiffFromBirthday()
        mvpView?.setCroppedImage(birthdayDetails?.imageUri)
    }

    fun calculateDiffFromBirthday() {
        val birthdayDateTime = DateTime(birthdayDetails?.birthdayTimestamp)
        val birthdayAge = when {
            birthdayDateTime.getYearsFromNow() > 0 -> BirthdayAge(birthdayDateTime.getYearsFromNow(), TimeType.YEARS)
            else -> BirthdayAge(birthdayDateTime.getMonthsFromNow(), TimeType.MONTHS)
        }
        val subTitle = getSubtitleByAge(birthdayAge)
        mvpView?.setBirthdaySubtitle(subTitle)
        mvpView?.setBirthdayAge(birthdayAge.age)
    }

    private fun getSubtitleByAge(birthdayAge: BirthdayAge): String {
        return when {
            birthdayAge.age >= 0 && birthdayAge.timeType == TimeType.MONTHS -> stringsMapper.getString(R.string.birthday_age_month)
            birthdayAge.age > 1 && birthdayAge.timeType == TimeType.MONTHS -> stringsMapper.getString(R.string.birthday_age_months)
            birthdayAge.age >= 0 && birthdayAge.timeType == TimeType.YEARS -> stringsMapper.getString(R.string.birthday_age_year)
            birthdayAge.age > 1 && birthdayAge.timeType == TimeType.YEARS -> stringsMapper.getString(R.string.birthday_age_years)
            else -> ""
        }
    }

    fun birthdayCameraIconClicked() {
        val intent = imagePicker.getPickIntent()
        mvpView?.showImagePicker(intent)
    }

    fun birthdayShareBtnClicked(bitmapFromView: Bitmap?) {
        val image = imagePicker.saveImage(bitmapFromView)
        mvpView?.shareImage(image)
    }

    fun imageFromPickerArrived(imageUri: Uri?) {
        if (imageUri != null) {
            imagePicker.imageUri = imageUri
        }
        imagePicker.imageUri?.let {
            mvpView?.setCroppedImage(imagePicker.imageUri)
        }
    }
}