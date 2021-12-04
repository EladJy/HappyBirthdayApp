package com.ej.happybirthdayapp.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

abstract class BaseFragment<T : BaseView> : Fragment() {

    protected abstract fun presenter(): BasePresenter<T>
    protected abstract fun mvpView(): T


    abstract val layoutId: Int

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(layoutId, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter().attachView(mvpView())
    }

    override fun onDestroyView() {
        presenter().detachView()
        super.onDestroyView()
    }
}