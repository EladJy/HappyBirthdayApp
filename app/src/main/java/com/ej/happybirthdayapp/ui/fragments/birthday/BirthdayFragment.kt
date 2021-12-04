package com.ej.happybirthdayapp.ui.fragments.birthday

import android.app.Activity
import android.content.Intent
import android.content.res.Resources
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.ej.happybirthdayapp.R
import com.ej.happybirthdayapp.databinding.FragmentBirthdayBinding
import com.ej.happybirthdayapp.model.BirthdayDetails
import com.ej.happybirthdayapp.ui.base.BaseFragment
import com.ej.happybirthdayapp.ui.base.BasePresenter
import com.ej.happybirthdayapp.ui.base.viewBinding
import com.ej.happybirthdayapp.ui.fragments.details.DetailsPresenter.Companion.BIRTHDAY_DETAILS
import com.ej.happybirthdayapp.utils.ImagePicker
import com.ej.happybirthdayapp.utils.getBitmapFromView
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class BirthdayFragment  : BaseFragment<BirthdayMvpView>(), BirthdayMvpView {

    private val viewBinding by viewBinding(FragmentBirthdayBinding::bind)

    override val layoutId: Int = R.layout.fragment_birthday

    private lateinit var startForResult: ActivityResultLauncher<Intent>

    @Inject
    lateinit var presenter: BirthdayPresenter

    override fun presenter(): BasePresenter<BirthdayMvpView> = presenter
    override fun mvpView(): BirthdayMvpView = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerActivityResult()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.setArguments(arguments?.get(BIRTHDAY_DETAILS) as BirthdayDetails?)
        initViews()
    }

    private fun initViews() {
        viewBinding.birthdayCameraIcon.setOnClickListener {
            presenter.birthdayCameraIconClicked()
        }
        viewBinding.birthdayGoBack.setOnClickListener {
            findNavController().popBackStack()
        }
        viewBinding.birthdayShareBtn.setOnClickListener {
            setViewsForSnapshot(false)
            val bitmap = viewBinding.birthdayContainer.getBitmapFromView()
            presenter.birthdayShareBtnClicked(bitmap)
        }
    }

    private fun setViewsForSnapshot(isVisible: Boolean) {
        viewBinding.birthdayShareBtn.isVisible = isVisible
        viewBinding.birthdayCameraIcon.isVisible = isVisible
        viewBinding.birthdayGoBack.isVisible = isVisible
    }

    override fun showImagePicker(intent: Intent?) {
        startForResult.launch(intent)
    }

    override fun setBirthdayTitle(birthdayTitle: String?) {
        viewBinding.birthdayTitle.text = birthdayTitle
    }

    override fun setBirthdaySubtitle(subTitle: String) {
        viewBinding.birthdaySubtitle.text = subTitle
    }

    override fun setBirthdayAge(age: Int) {
        val resources: Resources = requireActivity().resources
        val resourceId: Int = resources.getIdentifier("age_number_$age", "drawable", requireActivity().packageName)
        viewBinding.birthdayAge.setImageResource(resourceId)
    }

    private fun registerActivityResult() {
        startForResult =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
                if (result.resultCode == Activity.RESULT_OK) {
                    presenter.imageFromPickerArrived(result.data?.data)
                }
            }
    }

    override fun shareImage(image: Uri?) {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "image/jpeg"
        shareIntent.putExtra(Intent.EXTRA_STREAM, image)
        startActivity(Intent.createChooser(shareIntent, getString(R.string.share_the_news_text)))
        setViewsForSnapshot(true)
    }

    override fun setCroppedImage(imageUri: Uri?) {
        imageUri?.let {
            Glide.with(this)
                .load(imageUri)
                .circleCrop()
                .into(viewBinding.birthdayImage)
        }
    }

}