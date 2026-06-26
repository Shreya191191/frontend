package com.example.frontend.data.remote.api

import com.example.frontend.data.remote.dto.SearchVehiclesRequest
import com.example.frontend.data.remote.dto.VariantsRequest
import com.example.frontend.data.remote.dto.VehicleDetailsRequest
import com.example.frontend.data.remote.dto.VehicleDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface UserApi {

    @POST("api/user/showSingleofSameModel")
    suspend fun searchUniqueModels(
        @Body request: SearchVehiclesRequest
    ): Response<List<VehicleDto>>

    @POST("api/user/getVehiclesWithoutBooking")
    suspend fun getVehicleVariants(
        @Body request: VariantsRequest
    ): Response<List<VehicleDto>>

    @POST("api/user/showVehicleDetails")
    suspend fun getVehicleDetails(
        @Body request: VehicleDetailsRequest
    ): Response<VehicleDto>
}

