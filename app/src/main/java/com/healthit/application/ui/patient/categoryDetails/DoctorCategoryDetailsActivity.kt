package com.healthit.application.ui.patient.categoryDetails

import android.os.Build
import android.view.View
import androidx.activity.viewModels
import com.healthit.application.R
import com.healthit.application.base.activity.BaseActivity
import com.healthit.application.databinding.ActivityDoctorCategoryDetailsBinding
import com.healthit.application.model.DoctorsCategory
import com.healthit.application.ui.patient.doctorDetail.DoctorDetailsActivity
import com.healthit.application.utils.constant.HelperConstant
import com.healthit.application.utils.startActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DoctorCategoryDetailsActivity :
    BaseActivity<ActivityDoctorCategoryDetailsBinding, DoctorCategoryDetailViewModel>(R.layout.activity_doctor_category_details) {

    private val mViewModel: DoctorCategoryDetailViewModel by viewModels()

    private var mCategoryData: DoctorsCategory? = null
    private var mDoctorAdapter: DoctorsAdapter? = null

    override fun getBindingVariable(): Int = R.layout.activity_doctor_category_details

    override fun onInitDataBinding(viewBinding: ActivityDoctorCategoryDetailsBinding) {
        viewBinding.apply {
            setTitle()
            setRecyclerView(this)
            setClickListener(this)
            observer()
            mViewModel.fetchDoctor(mCategoryData?.name ?: "")
        }
    }

    override fun getViewModel(): DoctorCategoryDetailViewModel = mViewModel

    private fun setTitle() {
        mCategoryData = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(HelperConstant.IntentParams.sDOCTOR_CATEGORY,
                DoctorsCategory::class.java)
        } else {
            intent.getParcelableExtra(HelperConstant.IntentParams.sDOCTOR_CATEGORY) as DoctorsCategory?
        }
        mCategoryData?.let {
            getViewDataBinding().titleTv.text = it.name
        }
    }

    private fun setClickListener(viewBinding: ActivityDoctorCategoryDetailsBinding) {
        viewBinding.backIv.setOnClickListener {
            finish()
        }
    }

    private fun observer() {
        mViewModel.userResponse.observe(this) {
            if (it.isEmpty()) {
                getViewDataBinding().doctorRv.visibility = View.GONE
                getViewDataBinding().noResultTv.visibility = View.VISIBLE
            } else {
                getViewDataBinding().doctorRv.visibility = View.VISIBLE
                getViewDataBinding().noResultTv.visibility = View.GONE
                mDoctorAdapter?.submitList(it.toList())
            }
        }
    }

    private fun setRecyclerView(viewBinding: ActivityDoctorCategoryDetailsBinding) {
        mDoctorAdapter = DoctorsAdapter {
            startActivity<DoctorDetailsActivity> {
                putExtra(HelperConstant.IntentParams.sDOCTOR_DETAIL, it)
            }
        }
        viewBinding.doctorRv.adapter = mDoctorAdapter
    }

}