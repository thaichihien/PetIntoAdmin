package com.mobye.petintoadmin.models

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.RealmUUID
import io.realm.kotlin.types.annotations.PrimaryKey

class Notification(
    var title : String = "",
    var content : String = "",
    var type : String = "",
    var date : String = "",
    @PrimaryKey
    var id : RealmUUID = RealmUUID.random()
) : RealmObject {

    constructor() : this("","","",""){
    }
}