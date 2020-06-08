package com.androiddevs.coronastats.ui

import com.androiddevs.coronastats.ui.poko_classes.India
import retrofit2.Call
import retrofit2.http.GET

public interface indiaApi {
//    @GET("v2/key-value-stores/toDWvRj1JpTXiM8FF/records/LATEST?disableRedirect=true")
    @GET("total.json")
//    fun getFullData() : Call<AllData>
    fun getIndiaData() : Call<India>
}