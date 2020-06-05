package com.androiddevs.confused.ui

import com.google.gson.annotations.SerializedName

data class DistrictData (
	@SerializedName("district") val district : String,
	@SerializedName("notes") val notes : String,
	@SerializedName("active") val active : Int,
	@SerializedName("confirmed") val confirmed : Int,
	@SerializedName("deceased") val deceased : Int,
	@SerializedName("recovered") val recovered : Int,
	@SerializedName("delta") val delta : Delta
)