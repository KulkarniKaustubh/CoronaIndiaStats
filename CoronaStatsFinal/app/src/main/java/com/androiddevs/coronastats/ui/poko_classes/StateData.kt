package com.androiddevs.coronastats.ui.poko_classes

import com.google.gson.annotations.SerializedName

data class StateData (

	@SerializedName("state") val state : String,
	@SerializedName("active") val active : Int,
	@SerializedName("confirmed") val confirmed : Int,
	@SerializedName("recovered") val recovered : Int,
	@SerializedName("deaths") val deaths : Int,
	@SerializedName("districtData") val districtData : List<CityData>
)
