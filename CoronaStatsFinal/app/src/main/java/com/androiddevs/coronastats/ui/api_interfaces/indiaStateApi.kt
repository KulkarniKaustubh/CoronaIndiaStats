package com.androiddevs.coronastats.ui.api_interfaces

import com.androiddevs.coronastats.ui.poko_classes.RegionData
import retrofit2.Call
import retrofit2.http.GET

public interface indiaStateApi {
    @GET("state_data.json")
    fun getIndiaStateData() : Call<List<RegionData>>
}