package com.androiddevs.confused.ui

import org.json.JSONObject
import retrofit2.Call
import retrofit2.http.GET

public interface indiaApi {
    @GET("v2/key-value-stores/toDWvRj1JpTXiM8FF/records/LATEST?disableRedirect=true")
    fun getFullData() : Call<AllData>
}