package com.androiddevs.coronastats.ui

import com.google.gson.annotations.SerializedName

data class IndiaStateData (

	@SerializedName("region") val state : String,
	@SerializedName("totalInfected") val active : Int,
	@SerializedName("recovered") val recovered : Int,
	@SerializedName("deceased") val deceased : Int,
	@SerializedName("totalCases") val confirmed : Int
)
