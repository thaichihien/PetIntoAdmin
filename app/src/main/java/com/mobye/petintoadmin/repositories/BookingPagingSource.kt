package com.mobye.petintoadmin.repositories

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.mobye.petintoadmin.models.Booking
import com.mobye.petintoadmin.models.Product
import com.mobye.petintoadmin.network.RetrofitInstance

class BookingPagingSource(
    private val from : String,
    private val to : String,
    private val status : String
) : PagingSource<Int, Booking>() {

    companion object{
        const val FIRST_PAGE = 1        // bắt đầu từ trang 1
    }

    override fun getRefreshKey(state: PagingState<Int, Booking>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Booking> {
        return try {
            val nextPage = params.key ?: FIRST_PAGE //lấy chỉ số trang
            val response = RetrofitInstance.api.getBooking(nextPage,from,to,status)  // gọi hàm API lấy dữ liệu tương ứng

            //copy y hệt
            LoadResult.Page(
                data = response.body!!,
                prevKey = if(nextPage == BookingPagingSource.FIRST_PAGE) null else nextPage - 1,
                nextKey = if(response.body!!.isEmpty()) null else nextPage + 1
            )

        }catch (e: Exception){
            Log.e("BookingPagingSource",e.toString())   // thay đổi tag để debug
            LoadResult.Error(e)
        }
    }
}