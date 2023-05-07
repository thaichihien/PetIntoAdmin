package com.mobye.petintoadmin.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class Product  (
    var id : String = "",
    var name : String = "",
    var price : Int = 0,
    var typePet : String = "",
    var detail : String ="",
    var stock : Int = 0,
    var image : String = "",
    var quantity : Int = 0
) : Parcelable {
}