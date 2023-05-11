package com.mobye.petintoadmin.views.fragments

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.mobye.petintoadmin.R
import com.mobye.petintoadmin.databinding.FragmentAdminProfileBinding
import com.mobye.petintoadmin.repositories.ProfileRepository
import com.mobye.petintoadmin.utils.Utils
import com.mobye.petintoadmin.viewmodels.AdminViewModelFactory
import com.mobye.petintoadmin.viewmodels.ProfileViewModel
import com.mobye.petintoadmin.views.AuthenticationActivity
import com.mobye.petintoadmin.views.MainActivity


class AdminProfileFragment : BaseFragment<FragmentAdminProfileBinding>() {


    private val profileViewModel : ProfileViewModel by activityViewModels {
        AdminViewModelFactory(ProfileRepository())
    }

    private val firebaseAuth : FirebaseAuth by lazy { Firebase.auth }
    private val warningLogoutDialog : AlertDialog by lazy {
        Utils.createConfirmDialog(requireContext(),"Logout","Log out of the app ?"){
            logout()
        }
    }


    override fun setup() {

        (requireActivity() as MainActivity).showNav()

        profileViewModel.admin.observe(viewLifecycleOwner){
            binding.tvAdminName.text = it.name
        }

        binding.apply {
            btnLogout.setOnClickListener {
                warningLogoutDialog.show()
            }
            btnViewReports.setOnClickListener {
                findNavController().navigate(AdminProfileFragmentDirections.actionAdminProfileFragmentToReportManagementFragment())
            }
        }

    }


    private fun logout() {
        if(firebaseAuth.currentUser != null){
            firebaseAuth.signOut()

            val gotoLoginIntent = Intent(this@AdminProfileFragment.requireContext(), AuthenticationActivity::class.java)
            gotoLoginIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(gotoLoginIntent)
        }
    }

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentAdminProfileBinding
        get() = FragmentAdminProfileBinding::inflate

}