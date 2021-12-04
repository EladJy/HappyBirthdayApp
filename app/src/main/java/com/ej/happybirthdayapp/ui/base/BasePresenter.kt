package com.ej.happybirthdayapp.ui.base

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren

interface Presenter<in V: BaseView> {
    fun attachView(view: V)
    fun detachView()
}

open class BasePresenter<T: BaseView>: Presenter<T> {

    protected val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    var mvpView: T? = null
        private set

    override fun attachView(view: T) {
        scope.coroutineContext.cancelChildren()
        this.mvpView = view
    }

    override fun detachView() {
        mvpView = null
    }
}