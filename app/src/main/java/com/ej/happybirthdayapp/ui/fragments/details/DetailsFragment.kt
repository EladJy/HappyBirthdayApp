package com.ej.happybirthdayapp.ui.fragments.details

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.findNavController
import com.ej.happybirthdayapp.R
import com.ej.happybirthdayapp.databinding.FragmentDetailsBinding
import com.ej.happybirthdayapp.ui.base.BaseFragment
import com.ej.happybirthdayapp.ui.base.BasePresenter
import com.ej.happybirthdayapp.ui.base.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class DetailsFragment : BaseFragment<DetailsMvpView>(), DetailsMvpView {

    private val viewBinding by viewBinding(FragmentDetailsBinding::bind)

    override val layoutId: Int = R.layout.fragment_details

    private lateinit var startForResult: ActivityResultLauncher<Intent>

    @Inject
    lateinit var presenter: DetailsPresenter

    override fun presenter(): BasePresenter<DetailsMvpView> = presenter
    override fun mvpView(): DetailsMvpView = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerActivityResult()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setToolbar()
        initViews()
        setStatusBarColor(R.color.blue_bg)
    }

    private fun setToolbar() {
        viewBinding.detailsToolbar.title = getString(R.string.details_toolbar_title)
    }

    private fun initViews() {
        viewBinding.detailsDatePicker.setOnClickListener {
            presenter.datePickerClicked()
        }
        viewBinding.detailsImagePicker.setOnClickListener {
            presenter.imagePickerClicked()
        }
        viewBinding.detailsBirthdayScreen.setOnClickListener {
            val fullName = viewBinding.detailsNameET.text.toString()
            presenter.navigateToBirthdayScreenClicked(fullName)
        }

        viewBinding.detailsNameET.addTextChangedListener(afterTextChanged = { setBirthdayScreenBtnEnabled() })

        viewBinding.detailsDatePicker.addTextChangedListener(afterTextChanged = { setBirthdayScreenBtnEnabled() })
    }

    private fun setBirthdayScreenBtnEnabled() {
        viewBinding.detailsBirthdayScreen.isEnabled =
            viewBinding.detailsNameET.text.isNotEmpty() && viewBinding.detailsDatePicker.text.isNotEmpty()
    }

    override fun updateDatePicker(dateString: String) {
        viewBinding.detailsDatePicker.setText(dateString)
    }

    override fun showDatePicker(date: DatePickerDialog.OnDateSetListener,
                                calendar: Calendar,
                                maxDate: Long,
                                minDate: Long) {
        val datePickerDialog = DatePickerDialog(requireActivity(),
            date,
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH))
        datePickerDialog.datePicker.maxDate = maxDate
        datePickerDialog.datePicker.minDate = minDate
        datePickerDialog.show()
    }

    private fun registerActivityResult() {
        startForResult =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
                if (result.resultCode == Activity.RESULT_OK) {
                    presenter.imageFromPickerArrived(result.data?.data)
                } else {
                    presenter.removeImageUri()
                }
            }
    }

    override fun showImagePicker(intent: Intent?) {
        startForResult.launch(intent)
    }

    override fun setImageUri(imageUri: Uri?) {
        viewBinding.detailsImagePicker.setImageURI(imageUri)
    }

    override fun navigateToBirthdayScreen(bundle: Bundle) {
        findNavController().navigate(R.id.action_detailsFragment_to_birthdayFragment, bundle)
    }
}
