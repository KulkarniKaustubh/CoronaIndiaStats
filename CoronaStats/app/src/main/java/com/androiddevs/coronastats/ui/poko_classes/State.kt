package com.androiddevs.coronastats.ui

import com.google.gson.annotations.SerializedName

data class State (

	@SerializedName("state") val state : String,
	@SerializedName("statecode") val statecode : String,
	@SerializedName("districtData") val districtData : List<DistrictData>
)
