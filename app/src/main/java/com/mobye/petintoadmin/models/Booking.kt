package com.mobye.petintoadmin.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class Booking(
    var id : String = "",
    var CustomerId : String = "",
    var customerName : String = "",
    var phone : String = "",
    var petName: String = "",
    var genre : String = "",
    var weight : String = "",
    var type : String = "",
    var service : String = "",
    var checkIn : String = "",
    var checkOut : String = "",
    var status : String = "",
    var charge : Int = 0
) : Parcelable {
}