package com.mobye.petintoadmin.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.mobye.petintoadmin.models.Product
import com.mobye.petintoadmin.models.apimodels.ApiResponse
import com.mobye.petintoadmin.repositories.ProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch

class ProductViewModel(
    private val repository: ProductRepository
)  : ViewModel(){

    private val TAG = "ProductViewModel"

    private var _response : MutableLiveData<ApiResponse<*>> = MutableLiveData()
    val response get() = _response

    private val searchQuery : MutableStateFlow<String> by lazy { MutableStateFlow("") }
    val productItemList = searchQuery.flatMapLatest {query ->
        repository.getProductSource(query)
            .cachedIn(viewModelScope)
    }

    fun searchProduct(query : String){
        searchQuery.value = query
    }


    fun createProduct(product: Product){
        try {
            viewModelScope.launch {
                _response.value = repository.createProduct(product)
            }
        }catch (ex : Exception){
            Log.e(TAG,ex.toString())
        }
    }

    fun updateProduct(product: Product){
        try {
            viewModelScope.launch {
                _response.value = repository.updateProduct(product)
            }
        }catch (ex : Exception){
            Log.e(TAG,ex.toString())
        }
    }

    fun deleteProduct(product: Product){
        try {
            viewModelScope.launch {
                _response.value = repository.deleteProduct(product)
            }
        }catch (ex : Exception){
            Log.e(TAG,ex.toString())
        }
    }


}