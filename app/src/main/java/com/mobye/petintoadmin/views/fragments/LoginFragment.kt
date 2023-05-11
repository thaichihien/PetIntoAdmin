package com.mobye.petintoadmin.views.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.mobye.petintoadmin.R
import com.mobye.petintoadmin.databinding.FragmentLoginBinding
import com.mobye.petintoadmin.utils.Utils
import com.mobye.petintoadmin.views.AuthenticationActivity
import com.mobye.petintoadmin.views.MainActivity
import com.mobye.petintoadmin.views.changeToFail


class LoginFragment : BaseFragment<FragmentLoginBinding>() {

    private val firebaseAuth : FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    private val loadingDialog : AlertDialog by lazy {(activity as AuthenticationActivity).loadingDialog}
    private val notiDialog : Dialog by lazy { Utils.createNotificationDialog(requireContext()) }

    override fun setup() {

        binding.apply {
            btnSignIn.setOnClickListener {
                if(validate()){
                    login()
                }
            }
        }


    }

    private fun login() {
        loadingDialog.show()
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()


        firebaseAuth.signInWithEmailAndPassword(email,password)
            .addOnCompleteListener(requireActivity()){task ->
                loadingDialog.dismiss()
                if(task.isSuccessful){

                    goToMainActivity()
                }else{
                    // TODO show error
                    notiDialog.changeToFail(task.exception!!.message!!)
                    notiDialog.setOnCancelListener {

                    }
                    notiDialog.setOnDismissListener {

                    }
                    notiDialog.show()


                    Log.e("SignIn",task.exception.toString())
                }
            }
    }

    private fun validate(): Boolean {
        var isValidated = true
        if(binding.etEmail.text.isBlank()){
            binding.etEmail.error = "Please fill in a email"
            isValidated = false
        }else if(!Patterns.EMAIL_ADDRESS.matcher(binding.etEmail.text).matches()){
            binding.etEmail.error = "Please fill in a valid email"
            isValidated = false
        }else{
            binding.etEmail.error = null
        }


        if(binding.etPassword.text!!.isBlank()){
            binding.etPassword.error = "Please fill in a name"
            isValidated =false
        }else{
            binding.etPassword.error = null
        }

        return isValidated
    }

    private fun goToMainActivity(){
        val gotoMainIntent = Intent(this@LoginFragment.requireContext(), MainActivity::class.java)
        gotoMainIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(gotoMainIntent)
    }

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentLoginBinding
        get() = FragmentLoginBinding::inflate

}