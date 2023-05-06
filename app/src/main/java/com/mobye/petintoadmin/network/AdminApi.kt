package com.mobye.petintoadmin.network

import com.mobye.petintoadmin.models.Booking
import com.mobye.petintoadmin.models.Pet
import com.mobye.petintoadmin.models.Product
import com.mobye.petintoadmin.models.apimodels.ApiResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface AdminApi {

    // Product
    @GET("/admin/product/get")
    suspend fun getProduct(
        @Query("page") page : Int,
        @Query("query") query : String = ""
    ) : ApiResponse<List<Product>>

    @POST("/admin/product/create")
    suspend fun createProduct(
        @Body product : Product
    ) : ApiResponse<String>

    @POST("/admin/product/update")
    suspend fun updateProduct(
        @Body product : Product
    ) : ApiResponse<Any>

    @POST("/admin/product/delete")
    suspend fun deleteProduct(
        @Body product : Product
    ) : ApiResponse<Any>

    //Pet
    @GET("/admin/pet/get")
    suspend fun getPet(
        @Query("page") page : Int,
        @Query("query") query : String = ""
    ) : ApiResponse<List<Product>>

    @POST("/admin/pet/create")
    suspend fun createPet(
        @Body pet : Pet
    ) : ApiResponse<String>

    @POST("/admin/pet/update")
    suspend fun updatePet(
        @Body pet : Pet
    ) : ApiResponse<Any>

    @POST("/admin/pet/delete")
    suspend fun deletePet(
        @Body pet : Pet
    ) : ApiResponse<Any>

    //Booking
    @GET("/admin/pet/get")
    suspend fun getBooking(
        @Query("page") page : Int,
        @Query("query") query : String = ""
    ) : ApiResponse<List<Product>>

    @POST("/admin/pet/create")
    suspend fun createBooking(
        @Body booking: Booking
    ) : ApiResponse<Booking>

    @POST("/admin/pet/update")
    suspend fun updateBooking(
        @Body booking: Booking
    ) : ApiResponse<Any>

    @POST("/admin/pet/delete")
    suspend fun deleteBooking(
        @Body booking: Booking
    ) : ApiResponse<Any>

}