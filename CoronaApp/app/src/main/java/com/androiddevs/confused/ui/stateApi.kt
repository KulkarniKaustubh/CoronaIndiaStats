package com.androiddevs.confused.ui

import retrofit2.Call
import retrofit2.http.GET

public interface stateApi {
    @GET("v2/state_district_wise.json")
    fun getState() : Call<List<State>>
}