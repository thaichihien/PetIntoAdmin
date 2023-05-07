package com.mobye.petintoadmin.repositories

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.mobye.petintoadmin.models.Product
import com.mobye.petintoadmin.network.RetrofitInstance

//Lớp lấy dữ liệu bằng phân trang
class ProductPagingSource(
    private val query : String          //câu truy vấn tìm kiếm sản phẩm
) : PagingSource<Int, Product>() {      // <chỉ số là số nguyên nên Int, kiểu dữ liệu muốn lấy>
    companion object{
        const val FIRST_PAGE = 1        // bắt đầu từ trang 1
    }

    //copy y hệt
    override fun getRefreshKey(state: PagingState<Int, Product>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Product> {
        return try {
            val nextPage = params.key ?: FIRST_PAGE //lấy chỉ số trang
            val response = RetrofitInstance.api.getProduct(nextPage,query)  // gọi hàm API lấy dữ liệu tương ứng

            //copy y hệt
            LoadResult.Page(
                data = response.body!!,
                prevKey = if(nextPage == FIRST_PAGE) null else nextPage - 1,
                nextKey = if(response.body!!.isEmpty()) null else nextPage + 1
            )

        }catch (e: Exception){
            Log.e("ProductPagingSource",e.toString())   // thay đổi tag để debug
            LoadResult.Error(e)
        }
    }
}