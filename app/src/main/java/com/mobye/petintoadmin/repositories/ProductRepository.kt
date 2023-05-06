package com.mobye.petintoadmin.repositories

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.mobye.petintoadmin.models.Product
import com.mobye.petintoadmin.network.RetrofitInstance

class ProductRepository : IRepository {

    //Product
    fun getProductSource(query : String) = Pager(
        config = PagingConfig(pageSize = 10),
        pagingSourceFactory = {ProductPagingSource(query)})
        .flow

    suspend fun createProduct(product : Product)
        = RetrofitInstance.api.createProduct(product)


    suspend fun updateProduct(product: Product)
        = RetrofitInstance.api.updateProduct(product)

    suspend fun deleteProduct(product: Product)
        = RetrofitInstance.api.deleteProduct(product)



}