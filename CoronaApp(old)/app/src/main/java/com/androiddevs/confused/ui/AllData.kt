package com.androiddevs.confused.ui

import com.google.gson.annotations.SerializedName

data class AllData (

	@SerializedName("activeCases") val active : Int,
	@SerializedName("recovered") val recovered : Int,
	@SerializedName("deaths") val deaths : Int,
	@SerializedName("totalCases") val confirmed : Int,
	@SerializedName("lastUpdatedAtApify") val dateLastModified : String,
	@SerializedName("regionData") val regionData : List<RegionData>
)
