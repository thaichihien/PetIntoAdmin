package com.mobye.petintoadmin.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mobye.petintoadmin.repositories.*

@Suppress("UNCHECKED_CAST")
class AdminViewModelFactory(
    private val repository: IRepository
) : ViewModelProvider.NewInstanceFactory(){

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when(modelClass){
            ProductViewModel::class.java ->ProductViewModel(repository as ProductRepository) as T
            BookingViewModel::class.java -> BookingViewModel(repository as BookingRepository) as T
            OrderViewModel::class.java -> OrderViewModel(repository as OrderRepository) as T
            ProfileViewModel::class.java -> ProfileViewModel(repository as ProfileRepository) as T
            else -> throw IllegalArgumentException("Unknown View Model")
        }
    }
}