package com.healthit.application.ui.appointment.fragments

import android.view.View
import androidx.fragment.app.viewModels
import com.healthit.application.R
import com.healthit.application.base.dialogs.BaseBottomSheetDialogFragment
import com.healthit.application.databinding.BottomSheetMarkAsPaidFragmentBinding
import com.healthit.application.model.request.AppointmentModel
import com.healthit.application.ui.patient.dashboard.PatientMainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BottomSheetFragmentMarkAsPaid(
    val appointmentModel: AppointmentModel,
    private val markAsPaid: () -> Unit,
) : BaseBottomSheetDialogFragment<BottomSheetMarkAsPaidFragmentBinding, PatientMainViewModel>(R.layout.bottom_sheet_mark_as_paid_fragment) {

    companion object {
        const val TAG = "BottomSheetFragmentRequestSent::"
    }

    private val mViewModel: PatientMainViewModel by viewModels()

    override fun getViewModel(): PatientMainViewModel {
        return mViewModel
    }

    override fun onInitDataBinding(viewBinding: BottomSheetMarkAsPaidFragmentBinding) {
        dialog?.setCancelable(false)
        setUpClickListener(viewBinding)
    }

    private fun setUpClickListener(viewBinding: BottomSheetMarkAsPaidFragmentBinding) {
        with(viewBinding) {
            setOnItemClick {
                when (it.id) {
                    R.id.yesBtn -> {
                        markPaymentDone()
                    }
                    R.id.noBtn -> {
                        dismiss()
                    }
                }
            }
        }
        observers()
    }

    private fun observers() {
        mViewModel.updateAppointmentResponse.observe(viewLifecycleOwner) {
            if (it) with(getViewBinding()) {
                CoroutineScope(Dispatchers.Main).launch {
                    markPaidLottie.setAnimation("payment_success.json")
                    markPaidLottie.playAnimation()
                    markAsPaidTv.text = getString(R.string.success)
                    markAsPaidGroup.visibility = View.GONE
                    delay(3000)
                    markAsPaid.invoke()
                    dismiss()
                }
            }
        }
    }

    private fun markPaymentDone() {
        appointmentModel.paid = true
        mViewModel.markAsPaid(appointmentModel)
    }
}