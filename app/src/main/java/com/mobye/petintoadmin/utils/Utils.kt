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
import android.widget.TextView
import android.widget.Toast
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
        fun formatToLocalDate(ourDate: String,formatDate : String = "HH:mm dd-MM-yyyy") : String
                = try {
            val value: Date = Date.from(Instant.parse(ourDate))
            val dateFormatter = SimpleDateFormat(formatDate) //this format changeable
            dateFormatter.timeZone = TimeZone.getDefault()
            dateFormatter.format(value)
            //Log.d("ourDate", ourDate);
        } catch (e: Exception) {
            "00-00-0000 00:00"
        }

        fun getCurrentTimeString(pattern : String ="HH:mm dd-MM-yyyy") : String{
            val value = Calendar.getInstance().time
            val dateFormatter = SimpleDateFormat(pattern) //this format changeable
            dateFormatter.timeZone = TimeZone.getDefault()
            return dateFormatter.format(value)
        }

        fun getDateFromString(date : String) : Date
            = Date.from(Instant.parse(date))

        fun formatMoneyVND(amount : Int) : String
                = "%,d đ".format(amount)


        @SuppressLint("SimpleDateFormat")
        fun createSingleDatePicker(title : String,listener : (String,Date) -> Unit,formatDate : String = "dd/MM/yyyy",fromToday : Boolean =true) : MaterialDatePicker<Long> {
            val dayPickerBuilder = MaterialDatePicker.Builder.datePicker()
                .setTitleText(title)

            if(fromToday){
                dayPickerBuilder.setCalendarConstraints(
                    CalendarConstraints.Builder().setValidator(
                        DateValidatorPointForward.now()).build())
            }

            val dayPicker = dayPickerBuilder.build()

            dayPicker.addOnPositiveButtonClickListener {
                val calendar: Calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
                calendar.timeInMillis = it
                val format = SimpleDateFormat(formatDate)
                val formattedDate: String = format.format(calendar.time)
                listener(formattedDate,calendar.time)
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
                "All" -> 0
                "Waiting" -> 1
                "Prepare" -> 2
                "Delivering" -> 3
                "Delivered" -> 4
                "Cancelled" -> 5
            else -> 0
            }

        fun getIndexBookingStatus(status : String)
                = when(status){
            "All" -> 0
            "Waiting for reply" -> 1
            "Accepted" -> 2
            "Unaccepted" -> 3
            "Ready" -> 4
            "Cancelled" -> 5
            "Done" -> 6
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

        fun getIndexTimeSpa(time: String)
                = when(time){
            "08:00:00" -> 0
            "10:00:00" -> 1
            "15:00:00" -> 2
            "17:00:00" -> 3
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

        fun checkTextView(tv : TextView,context: Context) : Boolean{
            return if(tv.text.isBlank()){
                Toast.makeText(context,"Please input date",Toast.LENGTH_SHORT).show()
                false
            }else{

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

        fun formatStandardTimeString(date : Date,time : String) : String{
            val format = SimpleDateFormat("yyyy-MM-dd")
            val formattedDate: String = format.format(date)
            return "${formattedDate}T${time}"
        }


    }
}