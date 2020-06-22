package com.androiddevs.coronastats.ui.poko_classes

import com.google.gson.annotations.SerializedName

data class CityData (

	@SerializedName("name") val name : String,
	@SerializedName("confirmed") val confirmed : Int,
	@SerializedName("recovered") val recovered : String,
	@SerializedName("deaths") val deaths : String,
	@SerializedName("zone") val zone : String
)
