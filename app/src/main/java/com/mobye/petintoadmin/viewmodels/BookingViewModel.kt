package com.mobye.petintoadmin.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.mobye.petintoadmin.models.Booking
import com.mobye.petintoadmin.models.Product
import com.mobye.petintoadmin.models.apimodels.ApiResponse
import com.mobye.petintoadmin.repositories.BookingRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import java.util.*

class BookingViewModel(
    private val repository: BookingRepository
) : ViewModel(){

    private val TAG = "BookingViewModel"


    private var _response : MutableLiveData<ApiResponse<*>> = MutableLiveData()
    val response get() = _response

     val fromDate : MutableStateFlow<String> by lazy { MutableStateFlow("") }
     var toDate : String = ""
     var statusQuery : String = ""

    val scanBooking : MutableLiveData<Booking> by lazy { MutableLiveData() }

    //danh sách dữ liệu
    val bookingItemList = fromDate.flatMapLatest {from ->
        repository.getBookingSource(from,toDate,statusQuery)
            .cachedIn(viewModelScope)
    }

    fun filterBooking(from : String,to : String,status : String = ""){
        toDate = to
        statusQuery = status
        fromDate.value = from
    }

    fun refresh(){
        val temp = fromDate.value
        fromDate.value = "loading"
        fromDate.value = temp
    }

    fun clearFilter(){
        toDate = ""
        statusQuery = ""
        fromDate.value = ""
    }


    var checkIn  = Date()
    var checkOut = Date()


    //Tạo
    fun createBooking(booking: Booking){
        try {
            viewModelScope.launch {
                _response.value = repository.createBooking(booking)
            }
        }catch (ex : Exception){
            Log.e(TAG,ex.toString())
        }
    }

    //Sửa
    fun updateBooking(booking: Booking){
        try {
            viewModelScope.launch {
                _response.value = repository.updateBooking(booking)
            }
        }catch (ex : Exception){
            Log.e(TAG,ex.toString())
        }
    }


    //Xóa
    fun deleteBooking(booking: Booking){
        try {
            viewModelScope.launch {
                _response.value = repository.deleteBooking(booking)
            }
        }catch (ex : Exception){
            Log.e(TAG,ex.toString())
        }
    }

    fun getDetail(id: String){
        try {
            viewModelScope.launch {
                val res = repository.getDetail(id)
                if(res.result){
                    scanBooking.value = res.body
                }else{
                    scanBooking.value = Booking()
                }
            }
        }catch (ex : Exception){
            Log.e(TAG,ex.toString())
        }
    }
}