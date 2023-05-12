package com.mobye.petintoadmin.views.fragments

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.mobye.petintoadmin.R
import com.mobye.petintoadmin.adapters.NotificationItemAdapter
import com.mobye.petintoadmin.databinding.FragmentNotificationBinding
import com.mobye.petintoadmin.repositories.ProfileRepository
import com.mobye.petintoadmin.utils.Utils
import com.mobye.petintoadmin.viewmodels.AdminViewModelFactory
import com.mobye.petintoadmin.viewmodels.ProfileViewModel
import com.mobye.petintoadmin.views.MainActivity

class NotificationFragment : BaseFragment<FragmentNotificationBinding>() {

    private val profileViewModel : ProfileViewModel by activityViewModels {
        AdminViewModelFactory(ProfileRepository())
    }

    private lateinit var notificationAdapter : NotificationItemAdapter

    private val warningDeleteDialog : AlertDialog by lazy {
        Utils.createConfirmDialog(requireContext(),
            "Clear all notification",
            "Do you really want to clear all notification?"){
            clearNotification()
        }
    }



    override fun setup() {

        (requireActivity() as MainActivity).hideNav()

        notificationAdapter = NotificationItemAdapter(
            {i ->
                profileViewModel.removeNotification(i)
            },
            {
                handleNotification(it)
            }
        )

        profileViewModel.getNotification()
        profileViewModel.notificationList.observe(viewLifecycleOwner){
            notificationAdapter.differ.submitList(it)
        }

        binding.apply {
            rvNotification.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = notificationAdapter
            }
            btnClearAll.setOnClickListener {
                warningDeleteDialog.show()
            }
            btnBack.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

    private fun clearNotification() {
        profileViewModel.clearAllNotification()
    }

    private fun handleNotification(type: String) {
        when(type){
            "BOOKING" -> {
                findNavController().navigate(NotificationFragmentDirections.actionNotificationFragmentToBookingManagementFragment())
            }
            "ORDER","PET" -> {
                findNavController().navigate(NotificationFragmentDirections.actionNotificationFragmentToOrderManagementFragment())
            }
            "REPORT" ->{
                findNavController().navigate(NotificationFragmentDirections.actionNotificationFragmentToReportManagementFragment())
            }
        }
    }


    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentNotificationBinding
        get() = FragmentNotificationBinding::inflate
}