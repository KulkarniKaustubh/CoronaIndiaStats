package com.androiddevs.coronastats.ui.poko_classes

import com.google.gson.annotations.SerializedName

data class India (

	@SerializedName("active") val active : Int,
	@SerializedName("confirmed") val confirmed : Int,
	@SerializedName("recovered") val recovered : Int,
	@SerializedName("deaths") val deaths : Int
)
