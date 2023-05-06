package com.mobye.petintoadmin.utils

import android.os.Build
import androidx.annotation.RequiresApi
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
    }
}