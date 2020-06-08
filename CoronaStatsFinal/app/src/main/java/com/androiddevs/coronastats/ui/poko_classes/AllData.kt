package com.androiddevs.coronastats.ui

import com.google.gson.annotations.SerializedName

data class AllData (

    @SerializedName("activeCases") val active : Int,
    @SerializedName("recovered") val recovered : Int,
    @SerializedName("deaths") val deaths : Int,
    @SerializedName("totalCases") val confirmed : Int,
    @SerializedName("regionData") val regionData : List<IndiaStateData>,
    @SerializedName("lastUpdatedAtApify") val dateLastModified : String
)
