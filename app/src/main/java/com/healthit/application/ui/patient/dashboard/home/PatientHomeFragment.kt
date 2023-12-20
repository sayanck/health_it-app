package com.healthit.application.ui.patient.dashboard.home

import androidx.fragment.app.viewModels
import com.healthit.application.R
import com.healthit.application.base.fragment.BaseFragment
import com.healthit.application.databinding.FragmentPatientHomeBinding
import com.healthit.application.model.DoctorsCategory
import com.healthit.application.ui.patient.categoryDetails.DoctorCategoryDetailsActivity
import com.healthit.application.ui.patient.dashboard.PatientMainViewModel
import com.healthit.application.utils.constant.HelperConstant
import com.healthit.application.utils.startActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PatientHomeFragment :
    BaseFragment<FragmentPatientHomeBinding, PatientMainViewModel>(R.layout.fragment_patient_home) {

    private val mViewModel: PatientMainViewModel by viewModels()
    private var mCategoryAdapter: DoctorCategoriesAdapter? = null
    private val mCategoryDrawableList = arrayListOf(R.drawable.dermatoologist,
        R.drawable.dentist,
        R.drawable.pediatrician,
        R.drawable.neurologist,
        R.drawable.psychiatrist)

    private var doctorCategoryList: ArrayList<DoctorsCategory> = arrayListOf()

    override fun getViewModel(): PatientMainViewModel = mViewModel

    override fun onInitDataBinding(viewBinding: FragmentPatientHomeBinding) {
        setDoctorCategoryRecyclerView()
        setDataToAdapter()
    }

    private fun setDoctorCategoryRecyclerView() {
        with(getViewDataBinding()) {
            mCategoryAdapter = DoctorCategoriesAdapter {
                activity?.startActivity<DoctorCategoryDetailsActivity> {
                    putExtra(HelperConstant.IntentParams.sDOCTOR_CATEGORY, it)
                }
            }
            doctorCategoriesRv.adapter = mCategoryAdapter
        }
    }

    private fun setDataToAdapter() {
        val categories = activity?.resources?.getStringArray(R.array.doctorSpecialityList)
        doctorCategoryList.clear()
        for (i in 0 until 5) {
            val data = DoctorsCategory(name = categories?.get(i) ?: "",
                drawable = mCategoryDrawableList[i])
            doctorCategoryList.add(data)
        }
        mCategoryAdapter?.submitList(doctorCategoryList.toList())
    }

}