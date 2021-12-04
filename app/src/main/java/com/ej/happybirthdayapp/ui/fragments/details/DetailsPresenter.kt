package com.ej.happybirthdayapp.ui.fragments.details

import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import com.ej.happybirthdayapp.model.BirthdayDetails
import com.ej.happybirthdayapp.ui.base.BasePresenter
import com.ej.happybirthdayapp.ui.base.BaseView
import com.ej.happybirthdayapp.utils.ImagePicker
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

interface DetailsMvpView : BaseView {
    fun updateDatePicker(dateString: String)
    fun showDatePicker(date: DatePickerDialog.OnDateSetListener,
                       calendar: Calendar,
                       maxDate: Long,
                       minDate: Long)
    fun setImageUri(imageUri: Uri?)
    fun showImagePicker(intent: Intent?)
    fun navigateToBirthdayScreen(bundle: Bundle)
}

class DetailsPresenter @Inject constructor(private val imagePicker: ImagePicker) : BasePresenter<DetailsMvpView>() {

    private var birthdayDateTimestamp = 0L

    override fun attachView(view: DetailsMvpView) {
        super.attachView(view)
        if(imagePicker.imageUri != null) {
            mvpView?.setImageUri(imagePicker.imageUri)
        }
    }

    fun datePickerClicked() {
        val calendar = Calendar.getInstance()
        val date =
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth -> // TODO Auto-generated method stub
                calendar[Calendar.YEAR] = year
                calendar[Calendar.MONTH] = monthOfYear
                calendar[Calendar.DAY_OF_MONTH] = dayOfMonth
                birthdayDateTimestamp = calendar.timeInMillis
                val dateString = getFormattedDate(calendar)
                mvpView?.updateDatePicker(dateString)
            }
        val maxDate = Date().time
        val minDateCalendar = Calendar.getInstance()
        minDateCalendar.add(Calendar.YEAR, MIN_12_YEARS)
        mvpView?.showDatePicker(date, calendar, maxDate, minDateCalendar.timeInMillis)
    }

    private fun getFormattedDate(calendar: Calendar): String {
        val myFormat = "dd/MM/yyyy"
        val simpleDateFormat = SimpleDateFormat(myFormat, Locale.US)
        return simpleDateFormat.format(calendar.time)
    }

    fun imagePickerClicked() {
        val intent = imagePicker.getPickIntent()
        mvpView?.showImagePicker(intent)
    }

    fun imageFromPickerArrived(imageUri: Uri?) {
        if(imageUri != null) {
            imagePicker.imageUri = imageUri
        }
        imagePicker.imageUri?.let {
            mvpView?.setImageUri(imagePicker.imageUri)
        }
    }

    fun navigateToBirthdayScreenClicked(fullName: String) {
        val bundle = Bundle()
        val birthdayDetails = BirthdayDetails(fullName, birthdayDateTimestamp, imagePicker.imageUri)
        bundle.putParcelable(BIRTHDAY_DETAILS, birthdayDetails)
        mvpView?.navigateToBirthdayScreen(bundle)
    }

    companion object {
        const val MIN_12_YEARS = -12
        const val BIRTHDAY_DETAILS = "birthday_details"
    }
}