package com.ej.happybirthdayapp.ui.fragments.birthday

import com.ej.happybirthdayapp.R
import com.ej.happybirthdayapp.databinding.FragmentBirthdayBinding
import com.ej.happybirthdayapp.ui.base.BaseFragment
import com.ej.happybirthdayapp.ui.base.BasePresenter
import com.ej.happybirthdayapp.ui.base.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class BirthdayFragment  : BaseFragment<BirthdayMvpView>(), BirthdayMvpView {

    private val viewBinding by viewBinding(FragmentBirthdayBinding::bind)

    override val layoutId: Int = R.layout.fragment_birthday

    @Inject
    lateinit var presenter: BirthdayPresenter

    override fun presenter(): BasePresenter<BirthdayMvpView> = presenter
    override fun mvpView(): BirthdayMvpView = this


}