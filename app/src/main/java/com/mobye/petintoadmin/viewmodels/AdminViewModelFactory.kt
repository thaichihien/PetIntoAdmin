package com.mobye.petintoadmin.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mobye.petintoadmin.repositories.IRepository
import com.mobye.petintoadmin.repositories.ProductRepository

class AdminViewModelFactory(
    private val repository: IRepository
) : ViewModelProvider.NewInstanceFactory(){

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when(modelClass){
            ProductViewModel::class.java ->ProductViewModel(repository as ProductRepository) as T
            else -> throw IllegalArgumentException("Unknown View Model")
        }
    }
}