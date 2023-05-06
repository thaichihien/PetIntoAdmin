package com.mobye.petintoadmin.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class Pet(
    var id : String = "",
    var name : String = "",
    var price : Int = 0,
    var type : String = "",
    var image: String = "",
    var gender: String = "",
    var age: Int = 0,
    var vaccine: Int = 0,
    var variety: String = "",
    var weight: String = "",
    var color: String = ""
) : Parcelable {
}