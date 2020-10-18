package dev.arclight.weatherwarn.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Coord (
    @SerializedName("lon")
    @Expose
    var lon: Double?,

    @SerializedName("lat")
    @Expose
    var lat: Double?
)