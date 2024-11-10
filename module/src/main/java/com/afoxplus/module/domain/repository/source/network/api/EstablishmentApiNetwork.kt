package com.afoxplus.module.domain.repository.source.network.api

import com.afoxplus.module.domain.repository.source.network.api.request.FilterRequest
import com.afoxplus.module.domain.repository.source.network.api.response.EstablishmentResponse
import com.afoxplus.network.annotations.ServiceClient
import com.afoxplus.network.api.UrlProvider
import com.afoxplus.network.response.BaseResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

@ServiceClient(type = UrlProvider.Type.API_RESTAURANTS)
internal interface EstablishmentApiNetwork {
    companion object {
        const val PATH_RESTAURANT = "restaurant"
    }

    @POST("$PATH_RESTAURANT/filter")
    suspend fun fetchEstablishments(@Body query: FilterRequest): Response<BaseResponse<List<EstablishmentResponse>>>

    @GET("$PATH_RESTAURANT/types")
    suspend fun fetchEstablishmentTypes(): Response<BaseResponse<List<String>>>

}