package com.mobye.petintoadmin.repositories

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.mobye.petintoadmin.models.Pet
import com.mobye.petintoadmin.models.Product
import com.mobye.petintoadmin.network.RetrofitInstance

class PetPagingSource(
    private val query : String
) : PagingSource<Int, Pet>() {
    companion object{
        const val FIRST_PAGE = 1
    }

    //copy y hệt
    override fun getRefreshKey(state: PagingState<Int, Pet>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Pet> {
        return try {
            val nextPage = params.key ?: FIRST_PAGE
            val response = RetrofitInstance.api.getPet(nextPage,query)


            LoadResult.Page(
                data = response.body!!,
                prevKey = if(nextPage == FIRST_PAGE) null else nextPage - 1,
                nextKey = if(response.body!!.isEmpty()) null else nextPage + 1
            )

        }catch (e: Exception){
            Log.e("PetPagingSource",e.toString())
            LoadResult.Error(e)
        }
    }
}