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

    private val fromDate : MutableStateFlow<String> by lazy { MutableStateFlow("") }
    private var toDate : String = ""
    private var statusQuery : String = ""


    //danh sách dữ liệu
    val bookingItemList = fromDate.flatMapLatest {query ->
        repository.getBookingSource(query)
            .cachedIn(viewModelScope)
    }

    fun filterOrder(from : String,to : String,status : String = ""){
        toDate = to
        statusQuery = status
        fromDate.value = from
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
}