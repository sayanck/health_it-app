
package com.healthit.application.base.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import com.healthit.application.BR
import com.healthit.application.base.activity.BaseActivity
import com.healthit.application.base.viewmodel.BaseViewModel


abstract class BaseFragment<T : ViewDataBinding, V : BaseViewModel>(
    @LayoutRes private val layoutId: Int,
) : Fragment(), LifecycleOwner {

    private var mActivity: BaseActivity<T, V>? = null
    private lateinit var mViewDataBinding: T

    abstract fun onInitDataBinding(viewBinding: T)

    abstract fun getViewModel(): V

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is BaseActivity<*, *>) {
            mActivity = context as BaseActivity<T, V>
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mViewDataBinding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        mViewDataBinding.setVariable(BR.viewModel, getViewModel())
        mViewDataBinding.lifecycleOwner = viewLifecycleOwner
        return mViewDataBinding.root
    }

    override fun onDetach() {
        mActivity = null
        super.onDetach()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewDataBinding.executePendingBindings()
        initObservers()
        onInitDataBinding(mViewDataBinding)
    }

    private fun initObservers() {
        getViewModel().errorLiveData.observe(
            viewLifecycleOwner
        ) { error ->
            getBaseActivity()?.showSnackBar(error.peekContent())
        }

        getViewModel().messageLiveData.observe(
            viewLifecycleOwner
        ) { message ->
            getBaseActivity()?.showSnackBar(message.peekContent())
        }

        getViewModel().loadingLiveData.observe(
            viewLifecycleOwner
        ) { visible ->
            getBaseActivity()?.showProgress(visible.peekContent())
        }
    }

    fun showMessage(message: String) {
        getBaseActivity()?.showSnackBar(message)
    }

    fun onError(appError: String) {
        getViewModel().onError(appError)
    }

    fun showProgress(visible: Boolean) {
        getBaseActivity()?.showProgress(visible)
    }

    fun hideProgressBar() {
        getBaseActivity()?.hideProgressBar()
    }

    fun showToast(message: String?) {
        getBaseActivity()?.showToast(message)
    }

    open fun getBaseActivity(): BaseActivity<T, V>? {
        return mActivity
    }

    open fun getViewDataBinding(): T {
        return mViewDataBinding
    }
}