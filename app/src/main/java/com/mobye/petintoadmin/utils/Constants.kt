package com.mobye.petintoadmin.utils

class Constants {
    companion object{
        val orderStatus : List<String> = listOf(
            "Waiting","Prepare","Delivering",
            "Delivered","Cancelled"
        )

        val serviceBooking : List<String> = listOf(
            "Hotel", "Spa"
        )

        val typeBookingHotel : List<String> = listOf(
            "vip", "normal"
        )

        val typeBookingSpa : List<String> = listOf(
            "Hair cut", "Nail cut", "Bath", "Massage"
        )

        val statusBooking : List<String> = listOf(
            "Waiting for reply", "Accepted",
            "Unaccepted", "Cancelled", "Done"
        )

    }


}