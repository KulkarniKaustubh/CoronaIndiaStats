package com.androiddevs.coronastats.ui.apis

import com.androiddevs.coronastats.ui.poko_classes.IndiaStateData
import retrofit2.Call
import retrofit2.http.GET

public interface indiaStateApi {
    @GET("state_data.json")
    fun getIndiaStateData() : Call<List<IndiaStateData>>
}