package com.mobye.petintoadmin.views.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.mobye.petintoadmin.R
import com.mobye.petintoadmin.databinding.FragmentResetPasswordBinding
import com.mobye.petintoadmin.utils.Utils
import com.mobye.petintoadmin.views.AuthenticationActivity
import com.mobye.petintoadmin.views.changeToFail
import com.mobye.petintoadmin.views.changeToSuccess


class ResetPasswordFragment : BaseFragment<FragmentResetPasswordBinding>() {

    private val firebaseAuth : FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val loadingDialog : AlertDialog by lazy {
        val activity = requireActivity() as AuthenticationActivity
        activity.loadingDialog
    }
    private val notiDialog : Dialog by lazy {
        Utils.createNotificationDialog(requireContext())
    }

    override fun setup() {
        binding.apply {
            signupBtn.setOnClickListener {
                findNavController().popBackStack()
            }
            sendBtn.setOnClickListener {
                if(validate()){
                    sendResetPassword()
                }
            }
        }
    }

    private fun sendResetPassword() {
        loadingDialog.show()
        val email = binding.etEmail.text.toString().trim()

        firebaseAuth.sendPasswordResetEmail(email)
            .addOnCompleteListener(requireActivity()){task ->
                loadingDialog.dismiss()
                if(task.isSuccessful){
                    notiDialog.changeToSuccess(getString(R.string.send_email_success))
                    notiDialog.show()

                    finishSend()


                }else{
                    val notiError = when((task.exception as FirebaseAuthException).errorCode){
                        "ERROR_USER_NOT_FOUND" -> getString(R.string.not_found_account)
                        "ERROR_INVALID_EMAIL" -> getString(R.string.invalid_email)
                        else -> task.exception!!.message!!
                    }

                    notiDialog.changeToFail(notiError)
                    notiDialog.show()
                }

            }


    }

    private fun finishSend() {
        binding.apply {
            forgotPassword.text = getString(R.string.check_email_reset)
            etEmail.visibility = View.GONE
            sendBtn.visibility = View.GONE
        }
    }

    private fun validate(): Boolean {
        var isValidated = true
        if(binding.etEmail.text.isBlank()){
            binding.etEmail.error = getString(R.string.missing_email)
            isValidated = false
        }else if(!Patterns.EMAIL_ADDRESS.matcher(binding.etEmail.text).matches()){
            binding.etEmail.error = getString(R.string.invalid_email)
            isValidated = false
        }else{
            binding.etEmail.error = null
        }
        return isValidated
    }

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentResetPasswordBinding
        get() = FragmentResetPasswordBinding::inflate

}