package com.mobye.petintoadmin.viewmodels

import androidx.lifecycle.ViewModel
import com.mobye.petintoadmin.repositories.BookingRepository

class BookingViewModel(
    private val repository: BookingRepository
) : ViewModel(){
}