package com.ej.happybirthdayapp.ui

import androidx.navigation.Navigation
import com.ej.happybirthdayapp.R
import com.ej.happybirthdayapp.ui.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity() {
    override val layoutId: Int = R.layout.activity_main
    override fun onSupportNavigateUp(): Boolean =
        Navigation.findNavController(this, R.id.nav_host_fragment_container).navigateUp()
}