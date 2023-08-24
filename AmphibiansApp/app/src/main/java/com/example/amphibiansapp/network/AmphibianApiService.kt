package com.example.amphibiansapp.network

import com.example.amphibiansapp.model.AmphibianImage
import retrofit2.http.GET

interface AmphibianApiService {
    @GET("amphibians")
    suspend fun getAmphibians(): List<AmphibianImage>
}