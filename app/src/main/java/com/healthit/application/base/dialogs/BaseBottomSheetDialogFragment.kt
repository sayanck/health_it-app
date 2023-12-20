package com.healthit.application.base.dialogs

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.healthit.application.BR
import com.healthit.application.R
import com.healthit.application.base.activity.BaseActivity
import com.healthit.application.base.viewmodel.BaseViewModel

abstract class BaseBottomSheetDialogFragment<B : ViewDataBinding, M : BaseViewModel>(
    @LayoutRes
    private val layoutId: Int,
) : BottomSheetDialogFragment() {

    private lateinit var viewBinding: B

    fun getViewBinding(): B = viewBinding

    abstract fun onInitDataBinding(viewBinding: B)

    abstract fun getViewModel(): M

    override fun getTheme(): Int {
        return R.style.BottomSheetTopRoundedDialog
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        viewBinding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        viewBinding.setVariable(BR.viewModel, getViewModel())
        viewBinding.lifecycleOwner = viewLifecycleOwner
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservers()
        onInitDataBinding(viewBinding)
    }

    private fun initObservers() {
        getViewModel().errorLiveData.observe(
            viewLifecycleOwner
        ) { error ->
            val mError = error.contentIfNotHandled
            if (!TextUtils.isEmpty(mError)) {
                getBaseActivity().showSnackBar(error.peekContent())
            }
        }

        getViewModel().messageInDialogLiveData.observe(
            viewLifecycleOwner
        ) { message ->
            val mMessage = message.contentIfNotHandled

            if (!TextUtils.isEmpty(mMessage)) {
                getBaseActivity().showToast(message.peekContent())
            }
        }
        getViewModel().loadingLiveData.observe(
            viewLifecycleOwner
        ) { visible ->
            getBaseActivity().showProgress(visible.peekContent())
        }
    }

    fun showMessage(message: String) {
        getBaseActivity().showToast(message)
    }

    fun onError(appError: String) {
        getViewModel().onError(appError)
    }

    private fun showProgress(visible: Boolean) {
        getBaseActivity().showProgress(visible)
    }

    fun hideProgressBar() {
        getBaseActivity().hideProgressBar()
    }

    fun showToast(message: String?) {
        getBaseActivity().showToast(message)
    }

    private fun getBaseActivity() = requireActivity() as BaseActivity<*, *>
}