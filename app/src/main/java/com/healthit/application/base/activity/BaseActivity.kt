package com.healthit.application.base.activity

import android.app.ProgressDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.google.android.material.snackbar.Snackbar
import com.healthit.application.BR
import com.healthit.application.R
import com.healthit.application.base.viewmodel.BaseViewModel
import com.healthit.application.utils.KeyBoardUtils.hideKeyboard
import java.util.concurrent.atomic.AtomicBoolean

abstract class BaseActivity<T : ViewDataBinding, V : BaseViewModel>(
    @LayoutRes private val layoutId: Int,
) : AppCompatActivity() {

    private lateinit var mViewDataBinding: T
    private var progressDialog: ProgressDialog? = null
    var isProgressVisible = AtomicBoolean(false)


    abstract fun getBindingVariable(): Int
    abstract fun onInitDataBinding(viewBinding: T)

    abstract fun getViewModel(): V

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewDataBinding = DataBindingUtil.setContentView(this, layoutId)
        mViewDataBinding.setVariable(BR.viewModel, getViewModel())
        mViewDataBinding.lifecycleOwner = this
        initObservers()
        onInitDataBinding(mViewDataBinding)
    }

    fun getViewDataBinding(): T {
        return mViewDataBinding
    }

    fun showSnackBar(message: String) {
        hideProgressBar()
        hideKeyboard(this)
        Snackbar.make(
            getViewDataBinding().root,
            message,
            Snackbar.LENGTH_SHORT
        )
            .show()
    }

    private fun initObservers() {
        getViewModel().errorLiveData.observe(
            this
        ) { error ->
            showSnackBar(error.peekContent())
        }

        getViewModel().messageLiveData.observe(
            this
        ) { message ->
            showSnackBar(message.peekContent())
        }

        getViewModel().loadingLiveData.observe(
            this
        ) { visible ->
            showProgress(visible.peekContent())
        }
    }

    fun showProgress(visible: Boolean) {
        if (visible) {
            hideKeyboard(this)
            hideProgressBar()
            progressDialog = showLoadingDialog(this)
            isProgressVisible.set(true)

        } else {
            hideProgressBar()
            isProgressVisible.set(false)
        }
    }

    fun hideProgressBar() {
        progressDialog?.cancel()
    }

    private fun showLoadingDialog(context: Context?): ProgressDialog {
        val progressDialog = ProgressDialog(context)
        progressDialog.show()
        if (progressDialog.window != null) {
            progressDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            progressDialog.window!!.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        }

        progressDialog.setContentView(R.layout.progress_view)
        progressDialog.isIndeterminate = true
        progressDialog.setCancelable(false)
        progressDialog.setCanceledOnTouchOutside(false)
        return progressDialog
    }

    fun showToast(message: String?) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}