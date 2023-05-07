package com.mobye.petintoadmin.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.mobye.petintoadmin.models.Product
import com.mobye.petintoadmin.models.apimodels.ApiResponse
import com.mobye.petintoadmin.repositories.OrderRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch

class OrderViewModel(
    private val repository: OrderRepository
)  : ViewModel(){

    private val TAG = "OrderViewModel"

    private var _response : MutableLiveData<ApiResponse<*>> = MutableLiveData()
    val response get() = _response

    private val _productOrderList : MutableLiveData<List<Product>> by lazy { MutableLiveData(listOf()) }
    val productOrderList get() = _productOrderList

    private val fromDate : MutableStateFlow<String> by lazy { MutableStateFlow("") }
    private var toDate : String = ""
    private var statusQuery : String = ""



    //danh sách dữ liệu
    val orderItemList = fromDate.flatMapLatest {query ->
        repository.getOrderSource(query,toDate,statusQuery)
            .cachedIn(viewModelScope)
    }

    //thực hiện tìm kiếm
    fun filterOrder(from : String,to : String,status : String = ""){
        toDate = to
        statusQuery = status
        fromDate.value = from
    }


    fun getOrderDetail(id : String){
        try {
            viewModelScope.launch {
                val res = repository.getOrderDetail(id)
                if(res.result){
                    productOrderList.value = res.body
                }else{
                    Log.e(TAG,res.error)
                }


            }
        }catch (ex : Exception){
            Log.e(TAG,ex.toString())
        }
    }


}