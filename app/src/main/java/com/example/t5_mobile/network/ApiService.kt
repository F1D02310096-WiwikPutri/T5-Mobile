package com.example.t5_mobile.data.api

import com.example.t5_mobile.data.model.LoginResponse
import com.example.t5_mobile.data.model.PatientResponse
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @POST("api/login")
    suspend fun login(
        @Body credentials: Map<String, String>
    ): Response<LoginResponse>

    @GET("api/pasien")
    suspend fun getPatients(
        @Header("Authorization") authorization: String
    ): Response<PatientResponse>
}