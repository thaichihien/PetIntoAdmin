package com.mobye.petintoadmin.repositories

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.mobye.petintoadmin.models.Product
import com.mobye.petintoadmin.models.Report
import com.mobye.petintoadmin.network.RetrofitInstance

class ReportPagingSource: PagingSource<Int, Report>() {
    companion object{
        const val FIRST_PAGE = 1
    }

    override fun getRefreshKey(state: PagingState<Int, Report>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Report> {
        return try {
            val nextPage = params.key ?: FIRST_PAGE
            val response = RetrofitInstance.api.getReport(nextPage)

            //copy y hệt
            LoadResult.Page(
                data = response.body!!,
                prevKey = if(nextPage == FIRST_PAGE) null else nextPage - 1,
                nextKey = if(response.body!!.isEmpty()) null else nextPage + 1
            )

        }catch (e: Exception){
            Log.e("ReportPagingSource",e.toString())
            LoadResult.Error(e)
        }
    }
}