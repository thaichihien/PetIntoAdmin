package com.mobye.petintoadmin.views

import android.Manifest
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.mobye.petintoadmin.R
import com.mobye.petintoadmin.databinding.ActivityMainBinding
import com.mobye.petintoadmin.repositories.BookingRepository
import com.mobye.petintoadmin.repositories.ProfileRepository
import com.mobye.petintoadmin.viewmodels.AdminViewModelFactory
import com.mobye.petintoadmin.viewmodels.BookingViewModel
import com.mobye.petintoadmin.viewmodels.ProfileViewModel

fun Dialog.changeToFail(message : String){
    val ivIcon = this.findViewById<ImageView>(R.id.ivIcon)
    val tvResult = this.findViewById<TextView>(R.id.tvResult)
    val tvMessage = this.findViewById<TextView>(R.id.tvMessage)

    ivIcon.setImageResource(R.drawable.fail_icon)
    tvResult.text = "Something Wrong..."
    tvMessage.text = message
}

fun Dialog.changeToSuccess(message : String) {
    val ivIcon = this.findViewById<ImageView>(R.id.ivIcon)
    val tvResult = this.findViewById<TextView>(R.id.tvResult)
    val tvMessage = this.findViewById<TextView>(R.id.tvMessage)

    ivIcon.setImageResource(R.drawable.success_icon)
    tvResult.text = "Success"
    tvMessage.text = message
}
class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"

    private lateinit var binding : ActivityMainBinding

    private val profileViewModel : ProfileViewModel by viewModels {
        AdminViewModelFactory(ProfileRepository())
    }

    private lateinit var bottomNavView : BottomNavigationView
    private lateinit var navController : NavController

    private val firebaseAuth : FirebaseAuth by lazy { Firebase.auth }

    val loadingDialog : AlertDialog by lazy {  AlertDialog.Builder(this)
        .setCancelable(false)
        .setView(R.layout.loading_dialog)
        .create()
    }

    val notiDialog : Dialog by lazy {
        Dialog(this).apply {
            setCancelable(true)
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setContentView(R.layout.notification_dialog)
            findViewById<Button>(R.id.btnClose).setOnClickListener{
                this.dismiss()
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)




        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        window.statusBarColor = Color.TRANSPARENT


        if(firebaseAuth.currentUser != null){
            FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener {
                if(it.isSuccessful){
                    val token = it.result
                    //informationViewModel.getUser(firebaseAuth.uid!!,token)
                    profileViewModel.getAdmin(firebaseAuth.uid!!,token)
                    Log.e(TAG,"token : $token")
                }else{
                    Log.e(TAG,"token : FAIL")
                }
            })
        }else{
            val gotoMainIntent = Intent(this,AuthenticationActivity::class.java)
            gotoMainIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(gotoMainIntent)
        }

        askNotificationPermission()


        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainer) as NavHostFragment
        navController = navHostFragment.navController
        bottomNavView = binding.bottomNavView
        NavigationUI.setupWithNavController(bottomNavView, navController)

        onNewIntent(intent)
    }


    // Declare the launcher at the top of your Activity/Fragment:
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            // FCM SDK (and your app) can post notifications.
        } else {
            // TODO: Inform user that that your app will not show notifications.
        }
    }

    private fun askNotificationPermission() {
        // This is only necessary for API level >= 33 (TIRAMISU)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED
            ) {
                // FCM SDK (and your app) can post notifications.
            } else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                // TODO: display an educational UI explaining to the user the features that will be enabled
                //       by them granting the POST_NOTIFICATION permission. This UI should provide the user
                //       "OK" and "No thanks" buttons. If the user selects "OK," directly request the permission.
                //       If the user selects "No thanks," allow the user to continue without notifications.
            } else {
                // Directly ask for the permission
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    fun showNav(){
        bottomNavView.visibility = View.VISIBLE
    }

    fun hideNav(){
        bottomNavView.visibility = View.GONE
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        intent.extras?.let {
//            when(it.getString("fragment")){
//                "report" -> {
//                    navController.navigate(R.id.reportManagementFragment)
//                }
//            }
        }

    }


}