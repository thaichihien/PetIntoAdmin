package com.mobye.petintoadmin.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class Report (
    var id : String = "",
    var comment : String = "",
    var date : String = "",
    var name : String = "",
    var email : String = ""
) : Parcelable {
}