package com.ej.happybirthdayapp.ui.base

interface Presenter<in V: BaseView> {
    fun attachView(view: V)
    fun detachView()
}

open class BasePresenter<T: BaseView>: Presenter<T> {
    var mvpView: T? = null
        private set

    override fun attachView(view: T) {
        this.mvpView = view
    }

    override fun detachView() {
        mvpView = null
    }
}