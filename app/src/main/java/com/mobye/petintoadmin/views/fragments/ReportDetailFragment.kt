package com.mobye.petintoadmin.views.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.mobye.petintoadmin.R
import com.mobye.petintoadmin.databinding.FragmentReportDetailBinding
import com.mobye.petintoadmin.repositories.OrderRepository
import com.mobye.petintoadmin.repositories.ProductRepository
import com.mobye.petintoadmin.repositories.ProfileRepository
import com.mobye.petintoadmin.utils.Utils
import com.mobye.petintoadmin.viewmodels.AdminViewModelFactory
import com.mobye.petintoadmin.viewmodels.OrderViewModel
import com.mobye.petintoadmin.viewmodels.ProfileViewModel
import com.mobye.petintoadmin.views.changeToFail
import com.mobye.petintoadmin.views.changeToSuccess


class ReportDetailFragment : BaseFragment<FragmentReportDetailBinding>() {

    private val args : ReportDetailFragmentArgs by navArgs()
    private val profileViewModel : ProfileViewModel by activityViewModels {
        AdminViewModelFactory(ProfileRepository())
    }
    private val loadingDialog : AlertDialog by lazy { Utils.getLoadingDialog(requireActivity()) }
    private val notiDialog : Dialog by lazy { Utils.createNotificationDialog(requireContext()) }
    private val warningDeleteDialog : AlertDialog by lazy {
        val builder = AlertDialog.Builder(requireActivity())
        builder.apply {
            setMessage("Do you really want to delete this report?")
            setTitle("Delete")
            setPositiveButton("Yes") { _, _ ->
                sendDeleteReport()
            }
            setNegativeButton("No") { _, _ ->
                //nothing
            }
        }
        builder.create()
    }

    override fun setup() {
        fillData()

        binding.apply {
            btnBackDetail.setOnClickListener {
                findNavController().popBackStack()
            }

            btnDelete.setOnClickListener {
                warningDeleteDialog.show()
            }
        }



    }

    private fun fillData() {
        with(binding){
            val report = args.currentReport

            tvCustomerName.text = report.name
            tvEmail.text = report.email
            tvDate.text = Utils.formatToLocalDate(report.date)
            tvComment.text = report.comment
        }
    }


    private fun sendDeleteReport() {
        loadingDialog.show()

        profileViewModel.deleteReport(args.currentReport.id)

        profileViewModel.response.observe(viewLifecycleOwner){
            loadingDialog.dismiss()
            if(it.result){
                notiDialog.changeToSuccess(it.reason)
                notiDialog.setOnCancelListener{
                    findNavController().popBackStack()
                }
                notiDialog.setOnDismissListener{
                    findNavController().popBackStack()
                }
                notiDialog.show()

            }else{
                notiDialog.changeToFail(it.reason)
                notiDialog.setOnDismissListener(null)
                notiDialog.setOnCancelListener(null)
                notiDialog.show()
            }
        }
    }

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentReportDetailBinding
        get() = FragmentReportDetailBinding::inflate
}