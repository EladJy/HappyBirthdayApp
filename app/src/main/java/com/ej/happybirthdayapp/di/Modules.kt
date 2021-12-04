package com.ej.happybirthdayapp.di

import android.content.Context
import com.ej.happybirthdayapp.ui.MainActivity
import com.ej.happybirthdayapp.ui.fragments.birthday.BirthdayFragment
import com.ej.happybirthdayapp.ui.fragments.birthday.BirthdayMvpView
import com.ej.happybirthdayapp.ui.fragments.details.DetailsFragment
import com.ej.happybirthdayapp.ui.fragments.details.DetailsMvpView
import com.ej.happybirthdayapp.utils.ImagePicker
import com.ej.happybirthdayapp.utils.StringsMapper
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object SingletonModule {

    @Singleton
    @Provides
    fun provideImagePicker(@ApplicationContext context: Context): ImagePicker = ImagePicker(context)

    @Singleton
    @Provides
    fun stringMapper(@ApplicationContext context: Context) : StringsMapper = StringsMapper(context)
}

@InstallIn(ActivityComponent::class)
@Module
object BaseActivityModule {
    @Provides
    fun bindActivity(activity: ActivityComponent) : MainActivity {
        return activity as MainActivity
    }
}

@InstallIn(FragmentComponent::class)
@Module
abstract class FragmentModule {
    @Binds
    abstract fun bindDetailsFragment(fragment: DetailsFragment): DetailsMvpView

    @Binds
    abstract fun bindBirthdayFragment(fragment: BirthdayFragment) : BirthdayMvpView
}