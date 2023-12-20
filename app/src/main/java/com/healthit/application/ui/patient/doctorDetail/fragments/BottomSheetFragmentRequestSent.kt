package com.healthit.application.ui.patient.doctorDetail.fragments

import androidx.fragment.app.activityViewModels
import com.healthit.application.R
import com.healthit.application.base.dialogs.BaseBottomSheetDialogFragment
import com.healthit.application.databinding.BottomSheetRequestSentFragmentBinding
import com.healthit.application.ui.patient.doctorDetail.DoctorDetailsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BottomSheetFragmentRequestSent(private val onDoneClick: () -> Unit) :
    BaseBottomSheetDialogFragment<BottomSheetRequestSentFragmentBinding, DoctorDetailsViewModel>(R.layout.bottom_sheet_request_sent_fragment) {

    companion object {
        const val TAG = "BottomSheetFragmentRequestSent::"
    }

    private val mViewModel: DoctorDetailsViewModel by activityViewModels()

    override fun getViewModel(): DoctorDetailsViewModel {
        return mViewModel
    }

    override fun onInitDataBinding(viewBinding: BottomSheetRequestSentFragmentBinding) {
        dialog?.setCancelable(false)
        setUpClickListener(viewBinding)
    }

    private fun setUpClickListener(viewBinding: BottomSheetRequestSentFragmentBinding) {
        with(viewBinding) {
            setOnItemClick {
                when (it.id) {
                    R.id.btnDone -> {
                        onDoneClick.invoke()
                        dismiss()
                    }
                }
            }
        }
    }
}