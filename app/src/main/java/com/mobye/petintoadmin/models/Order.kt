package com.mobye.petintoadmin.models

class Order(
    var id : String = "",
    var address : String = "",
    var note : String = "",
    var dateDelivery : String = "",
    var payment : String = "",
    var isdelivery : String = "",
    var customerName : String = "",
    var phone : String = "",
    var status : String = "",
    var customerId : String = "",
    var amount : Int = 0,
    var total : Int = 0,
    var orderDate : String = "",
    var petId : String = ""
) {
}