package com.mobye.petintoadmin.views

import android.app.AlertDialog
import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.mobye.petintoadmin.R
import com.mobye.petintoadmin.databinding.ActivityMainBinding

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

    private lateinit var binding : ActivityMainBinding

    lateinit var bottomNavView : BottomNavigationView

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

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainer) as NavHostFragment
        val navController = navHostFragment.navController
        bottomNavView = binding.bottomNavView
        NavigationUI.setupWithNavController(bottomNavView, navController)
    }

    fun showNav(){
        bottomNavView.visibility = View.VISIBLE
    }

    fun hideNav(){
        bottomNavView.visibility = View.GONE
    }


}