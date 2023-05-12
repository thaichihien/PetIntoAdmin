package com.mobye.petintoadmin.views.fragments

import android.app.AlertDialog
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions
import com.mobye.petintoadmin.databinding.FragmentAdminProfileBinding
import com.mobye.petintoadmin.repositories.BookingRepository
import com.mobye.petintoadmin.repositories.ProfileRepository
import com.mobye.petintoadmin.utils.Utils
import com.mobye.petintoadmin.viewmodels.AdminViewModelFactory
import com.mobye.petintoadmin.viewmodels.BookingViewModel
import com.mobye.petintoadmin.viewmodels.ProfileViewModel
import com.mobye.petintoadmin.views.AuthenticationActivity
import com.mobye.petintoadmin.views.MainActivity


class AdminProfileFragment : BaseFragment<FragmentAdminProfileBinding>() {


    private val profileViewModel : ProfileViewModel by activityViewModels {
        AdminViewModelFactory(ProfileRepository())
    }
    private val bookingViewModel : BookingViewModel by activityViewModels {
        AdminViewModelFactory(BookingRepository())
    }

    private val firebaseAuth : FirebaseAuth by lazy { Firebase.auth }
    private val loadingDialog : AlertDialog by lazy { Utils.getLoadingDialog(requireActivity()) }
    private val warningLogoutDialog : AlertDialog by lazy {
        Utils.createConfirmDialog(requireContext(),"Logout","Log out of the app ?"){
            logout()
        }
    }

    private val barcodeLauncher = registerForActivityResult(
        ScanContract()
    ) { result: ScanIntentResult ->
        if (result.contents == null) {
            Toast.makeText(requireContext(), "Cancel Scan", Toast.LENGTH_LONG).show()
        } else {
            openBookingDetail(result.contents)
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
            btnScanQR.setOnClickListener {
                barcodeLauncher.launch(ScanOptions())
            }
        }


    }


    private fun openBookingDetail(id: String) {
        loadingDialog.show()
        bookingViewModel.getDetail(id)

        bookingViewModel.scanBooking.observe(viewLifecycleOwner){
            loadingDialog.dismiss()
            if(it.id.isNotBlank()){
                findNavController().navigate(AdminProfileFragmentDirections.actionAdminProfileFragmentToBookingDetailsFragment(it))
            }else{
                Toast.makeText(requireContext(),"Not found",Toast.LENGTH_SHORT).show()
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