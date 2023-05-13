package com.mobye.petintoadmin.repositories

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.mobye.petintoadmin.models.Booking
import com.mobye.petintoadmin.network.RetrofitInstance

class BookingRepository : IRepository {
    fun getBookingSource(
            from : String,to : String, status : String
                         ) = Pager(
        config = PagingConfig(pageSize = 10),
        pagingSourceFactory = {BookingPagingSource(from, to, status)})
        .flow

    //hàm thêm
    suspend fun createBooking(booking: Booking)
            = RetrofitInstance.api.createBooking(booking)

    //Hàm cập nhật
    suspend fun updateBooking(booking: Booking)
            = RetrofitInstance.api.updateBooking(booking)

    //Hàm xóa
    suspend fun deleteBooking(booking: Booking)
            = RetrofitInstance.api.deleteBooking(booking)

    suspend fun getDetail(id: String)
            = RetrofitInstance.api.getBookingDetail(id)
}