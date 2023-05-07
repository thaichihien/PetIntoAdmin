package com.mobye.petintoadmin.repositories

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.mobye.petintoadmin.models.Order
import com.mobye.petintoadmin.network.RetrofitInstance

class OrderPagingSource (
    private val from : String,
    private val to : String,
    private val status : String,
) : PagingSource<Int, Order>() {
    companion object{
        const val FIRST_PAGE = 1
    }


    override fun getRefreshKey(state: PagingState<Int, Order>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Order> {
        return try {
            val nextPage = params.key ?: FIRST_PAGE
            val response = RetrofitInstance.api.getOrder(nextPage,from,to,status)

            //copy y hệt
            LoadResult.Page(
                data = response.body!!,
                prevKey = if(nextPage == FIRST_PAGE) null else nextPage - 1,
                nextKey = if(response.body!!.isEmpty()) null else nextPage + 1
            )

        }catch (e: Exception){
            Log.e("ProductPagingSource",e.toString())
            LoadResult.Error(e)
        }
    }
}