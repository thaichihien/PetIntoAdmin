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

    private val TAG = "ProductViewModel"    // dùng cho debug

    //dùng để nhận kết quả từ Server và thông báo lên UI
    private var _response : MutableLiveData<ApiResponse<*>> = MutableLiveData()
    val response get() = _response

    //chuỗi tìm kiếm , rỗng là lấy tất cả
    private val searchQuery : MutableStateFlow<String> by lazy { MutableStateFlow("") }

    //danh sách dữ liệu
    val productItemList = searchQuery.flatMapLatest {query ->
        repository.getProductSource(query)
            .cachedIn(viewModelScope)
    }

    //thực hiện tìm kiếm
    fun searchProduct(query : String){
        searchQuery.value = query
    }


    //Tạo
    fun createProduct(product: Product){
        try {
            viewModelScope.launch {
                _response.value = repository.createProduct(product)
            }
        }catch (ex : Exception){
            Log.e(TAG,ex.toString())
        }
    }

    //Sửa
    fun updateProduct(product: Product){
        try {
            viewModelScope.launch {
                _response.value = repository.updateProduct(product)
            }
        }catch (ex : Exception){
            Log.e(TAG,ex.toString())
        }
    }


    //Xóa
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