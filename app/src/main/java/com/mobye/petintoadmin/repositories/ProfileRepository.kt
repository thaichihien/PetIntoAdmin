package com.mobye.petintoadmin.repositories

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.mobye.petintoadmin.network.RetrofitInstance

class ProfileRepository : IRepository {


    suspend fun getAdmin(id : String,token : Map<String, String>)
        = RetrofitInstance.api.getAdmin(id,token)

    fun getReportSource() = Pager(
        config = PagingConfig(pageSize = 10),
        pagingSourceFactory = {ReportPagingSource()})
        .flow

    suspend fun deleteReport(id : String)
        = RetrofitInstance.api.deleteReport(id)
}