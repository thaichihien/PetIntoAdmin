package com.mobye.petintoadmin.models.apimodels

import com.mobye.petintoadmin.models.Order
import com.mobye.petintoadmin.models.Product

class OrderCart(
    var cart : List<Product> = listOf()
) : Order() {



}