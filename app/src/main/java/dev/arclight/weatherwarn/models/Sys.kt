package dev.arclight.weatherwarn.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Sys (
    @SerializedName("sunrise")
    @Expose
    var sunrise: Int?,

    @SerializedName("sunset")
    @Expose
    var sunset: Int?
)