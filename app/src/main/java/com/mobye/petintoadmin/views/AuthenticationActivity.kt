package com.mobye.petintoadmin.views

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.mobye.petintoadmin.R
import com.mobye.petintoadmin.databinding.ActivityAuthenticationBinding

class AuthenticationActivity : AppCompatActivity() {

    private lateinit var binding : ActivityAuthenticationBinding
    private val firebaseAuth : FirebaseAuth by lazy { Firebase.auth }

    val loadingDialog : AlertDialog by lazy {  AlertDialog.Builder(this)
        .setCancelable(false)
        .setView(R.layout.loading_dialog)
        .create()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        binding =ActivityAuthenticationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val checkUser = firebaseAuth.currentUser
        if(checkUser != null){
            val gotoMainIntent = Intent(this,MainActivity::class.java)
            gotoMainIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(gotoMainIntent)
        }


    }
}