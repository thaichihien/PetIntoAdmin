package com.mobye.petintoadmin.repositories

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.mobye.petintoadmin.models.Product
import com.mobye.petintoadmin.network.RetrofitInstance


class ProductPagingSource(
    private val query : String
) : PagingSource<Int, Product>() {
    companion object{
        const val FIRST_PAGE = 1
    }
    override fun getRefreshKey(state: PagingState<Int, Product>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Product> {
        return try {
            val nextPage = params.key ?: FIRST_PAGE
//            val queryMap = mapOf<String,String>(
//                Pair("page",nextPage.toString()),
//                Pair("query",query)
//            )

            val response = RetrofitInstance.api.getProduct(nextPage,query)


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