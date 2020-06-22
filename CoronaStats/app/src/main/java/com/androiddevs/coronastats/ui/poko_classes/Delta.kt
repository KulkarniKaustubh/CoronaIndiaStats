package com.androiddevs.coronastats.ui

import com.google.gson.annotations.SerializedName

data class Delta (

	@SerializedName("confirmed") val confirmed : Int,
	@SerializedName("deceased") val deceased : Int,
	@SerializedName("recovered") val recovered : Int
)