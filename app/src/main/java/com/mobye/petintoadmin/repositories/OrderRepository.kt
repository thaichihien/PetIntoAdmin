package com.mobye.petintoadmin.repositories

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.mobye.petintoadmin.models.Order
import com.mobye.petintoadmin.network.RetrofitInstance

class OrderRepository : IRepository {

    fun getOrderSource(from : String,to : String,status : String)
        =  Pager(
        config = PagingConfig(pageSize = 10),
        pagingSourceFactory = {OrderPagingSource(from,to,status)})
        .flow


    suspend fun getOrderDetail(id : String)
        = RetrofitInstance.api.getOrderDetail(id)

    suspend fun createOrder(order : Order)
            = RetrofitInstance.api.createOrder(order)


    suspend fun updateOrder(order: Order)
            = RetrofitInstance.api.updateOrder(order)


    suspend fun deleteOrder(order: Order)
            = RetrofitInstance.api.deleteOrder(order)


}