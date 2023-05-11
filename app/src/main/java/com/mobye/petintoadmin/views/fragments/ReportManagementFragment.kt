package com.mobye.petintoadmin.views.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.mobye.petintoadmin.R
import com.mobye.petintoadmin.adapters.ProductPagingAdapter
import com.mobye.petintoadmin.adapters.ReportPagingAdapter
import com.mobye.petintoadmin.databinding.FragmentReportManagementBinding
import com.mobye.petintoadmin.repositories.ProductRepository
import com.mobye.petintoadmin.repositories.ProfileRepository
import com.mobye.petintoadmin.viewmodels.AdminViewModelFactory
import com.mobye.petintoadmin.viewmodels.ProductViewModel
import com.mobye.petintoadmin.viewmodels.ProfileViewModel
import com.mobye.petintoadmin.views.MainActivity
import kotlinx.coroutines.flow.collectLatest


class ReportManagementFragment : BaseFragment<FragmentReportManagementBinding>() {

    private val profileViewModel : ProfileViewModel by activityViewModels {
        AdminViewModelFactory(ProfileRepository())
    }
    private lateinit var reportAdapter : ReportPagingAdapter


    override fun setup() {

        (requireActivity() as MainActivity).hideNav()

        reportAdapter = ReportPagingAdapter {
            findNavController().navigate(ReportManagementFragmentDirections.actionReportManagementFragmentToReportDetailFragment(it))
        }

        reportAdapter.refresh()
        lifecycleScope.launchWhenCreated {
            profileViewModel.reportList.collectLatest {
               reportAdapter.submitData(it)
            }
        }

        binding.apply {

            rvReport.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = reportAdapter
            }
            btnBackDetail.setOnClickListener {
                findNavController().popBackStack()
            }
        }

    }

//    override fun onAttach(context: Context) {
//        super.onAttach(context)
//
//        val callback = object : OnBackPressedCallback(true){
//            override fun handleOnBackPressed() {
//                backToProfile()
//            }
//        }
//
//        requireActivity().onBackPressedDispatcher.addCallback(this,callback)
//    }
//
//    private fun backToProfile() {
//        findNavController().popBackStack(R.id.adminProfileFragment,false)
//    }

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentReportManagementBinding
        get() = FragmentReportManagementBinding::inflate

}