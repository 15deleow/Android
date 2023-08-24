package com.example.amphibiansapp.data

import com.example.amphibiansapp.network.AmphibianApiService
import com.example.amphibiansapp.model.AmphibianImage

/**
 * Repository retrieves amphibian data from underlying data source.
 */
interface AmphibianImagesRepository {
    suspend fun getAmphibians() : List<AmphibianImage>
}

class NetworkAmphibianImagesRepository(
    private val amphibianApiService: AmphibianApiService
) : AmphibianImagesRepository {
    override suspend fun getAmphibians(): List<AmphibianImage> = amphibianApiService.getAmphibians()
}