package com.ej.happybirthdayapp.ui.fragments.birthday

import com.ej.happybirthdayapp.ui.base.BasePresenter
import com.ej.happybirthdayapp.ui.base.BaseView
import javax.inject.Inject

interface BirthdayMvpView : BaseView {

}

class BirthdayPresenter @Inject constructor() : BasePresenter<BirthdayMvpView>() {
}