package com.mobye.petintoadmin.repositories

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.mobye.petintoadmin.models.Pet
import com.mobye.petintoadmin.models.Product
import com.mobye.petintoadmin.network.RetrofitInstance

class ProductRepository : IRepository {

    //Product

    //Hàm lấy danh sách dữ liệu (xem)
    fun getProductSource(query : String) = Pager(
        config = PagingConfig(pageSize = 10),
        pagingSourceFactory = {ProductPagingSource(query)})
        .flow

    //hàm thêm
    suspend fun createProduct(product : Product)
        = RetrofitInstance.api.createProduct(product)

    //Hàm cập nhật
    suspend fun updateProduct(product: Product)
        = RetrofitInstance.api.updateProduct(product)

    //Hàm xóa
    suspend fun deleteProduct(product: Product)
        = RetrofitInstance.api.deleteProduct(product)


    //Pet viết phần lấy dữ liệu pet dưới này
    //Danh sách
    fun getPetSource(query : String) = Pager(
        config = PagingConfig(pageSize = 10),
        pagingSourceFactory = {PetPagingSource(query)})
        .flow

    //hàm thêm
    suspend fun createPet(Pet : Pet)
            = RetrofitInstance.api.createPet(Pet)

    //Hàm cập nhật
    suspend fun updatePet(Pet: Pet)
            = RetrofitInstance.api.updatePet(Pet)

    //Hàm xóa
    suspend fun deletePet(Pet: Pet)
            = RetrofitInstance.api.deletePet(Pet)

}