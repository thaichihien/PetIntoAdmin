package com.mobye.petintoadmin.repositories

import com.mobye.petintoadmin.network.RetrofitInstance

class ProfileRepository : IRepository {


    suspend fun getAdmin(id : String,token : Map<String, String>)
        = RetrofitInstance.api.getAdmin(id,token)
}