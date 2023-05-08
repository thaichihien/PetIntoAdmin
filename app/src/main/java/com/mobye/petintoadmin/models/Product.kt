package com.mobye.petintoadmin.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class Product  (
    var id : String = "",
    var name : String = "",
    var price : Int = -1,
    var typePet : String = "",
    var detail : String ="",
    var stock : Int = -1,
    var image : String = "",
    var quantity : Int = -1
) : Parcelable {
}