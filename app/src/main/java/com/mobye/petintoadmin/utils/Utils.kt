package com.mobye.petintoadmin.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Build
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.annotation.RequiresApi
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.mobye.petintoadmin.R
import com.mobye.petintoadmin.views.MainActivity
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.*

class Utils {
    companion object{
        @RequiresApi(Build.VERSION_CODES.O)
        fun formatToLocalDate(ourDate: String) : String
                = try {
            val value: Date = Date.from(Instant.parse(ourDate))
            val dateFormatter = SimpleDateFormat("HH:mm MM-dd-yyyy") //this format changeable
            dateFormatter.timeZone = TimeZone.getDefault()
            dateFormatter.format(value)
            //Log.d("ourDate", ourDate);
        } catch (e: Exception) {
            "00-00-0000 00:00"
        }

        fun formatMoneyVND(amount : Int) : String
                = "%,d đ".format(amount)


        @SuppressLint("SimpleDateFormat")
        fun createSingleDatePicker(title : String,listener : (String) -> Unit,formatDate : String = "dd/MM/yyyy") : MaterialDatePicker<Long> {
            val dayPicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText(title)
                .setCalendarConstraints(
                    CalendarConstraints.Builder().setValidator(
                        DateValidatorPointForward.now()).build())
                .build()

            dayPicker.addOnPositiveButtonClickListener {
                val calendar: Calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
                calendar.timeInMillis = it
                val format = SimpleDateFormat(formatDate)
                val formattedDate: String = format.format(calendar.time)
                listener(formattedDate)
            }

            return dayPicker
        }

        fun getLoadingDialog(activity: Activity)
            = (activity as MainActivity).loadingDialog

        fun createNotificationDialog(context : Context)
            =  Dialog(context).apply {
            setCancelable(true)
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setContentView(R.layout.notification_dialog)
            findViewById<Button>(R.id.btnClose).setOnClickListener{
                this.dismiss()
            }
        }

        fun createConfirmDialog(context : Context,title : String,message : String,listener : () -> Unit) : AlertDialog {
            val builder = AlertDialog.Builder(context)
            builder.apply {
                setMessage(message)
                setTitle(title)
                setPositiveButton("Yes") { _, _ ->
                    listener()
                }
                setNegativeButton("No") { _, _ ->
                    //nothing
                }
            }
            return builder.create()
        }


        fun getIndexOrderStatus(status : String)
            = when(status){
                "Waiting" -> 0
                "Prepare" -> 1
                "Delivering" -> 2
                "Delivered" -> 3
                "Cancelled" -> 4
            else -> 0
            }

        fun getIndexBookingStatus(status : String)
                = when(status){
            "Waiting for reply" -> 0
            "Accepted" -> 1
            "Unaccepted" -> 2
            "Cancelled" -> 3
            "Done" -> 4
            else -> 0
        }

        fun getIndexBookingTypeHotel(status : String)
                = when(status){
            "vip" -> 0
            "normal" -> 1
            else -> 0
        }

        fun getIndexBookingTypeSpa(status : String)
                = when(status){
            "Hair" -> 0
            "Nail cut" -> 1
            "Bath" -> 2
            else -> 0
        }

        fun getIndexService(status: String)
            = when(status){
                "Hotel" -> 0
                "Spa" -> 1
                else -> 0
            }


        fun checkEditText(et : EditText) : Boolean{
            return if(et.text.isBlank()){
                et.error = "Please fill in"
                false
            }else{
                et.error = null
                true
            }
        }

        fun checkRadioGroup(rg : RadioGroup,lastRadioButton : RadioButton) : Boolean{
            return if(rg.checkedRadioButtonId < 0){
                lastRadioButton.error = "Please choose a payment method"
                false
            }else{
                lastRadioButton.error = null
                true
            }
        }
    }
}