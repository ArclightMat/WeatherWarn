package dev.arclight.weatherwarn.models

import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

data class Clouds (
    @SerializedName("all")
    @Expose
    var all: Int?
)