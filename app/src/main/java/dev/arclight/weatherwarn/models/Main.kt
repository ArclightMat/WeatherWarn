package dev.arclight.weatherwarn.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Main (
    @SerializedName("temp")
    @Expose
    var temp: Double?,

    @SerializedName("feels_like")
    @Expose
    var feelsLike: Double?,

    @SerializedName("temp_min")
    @Expose
    var tempMin: Double?,

    @SerializedName("temp_max")
    @Expose
    var tempMax: Double?,

    @SerializedName("pressure")
    @Expose
    var pressure: Int?,

    @SerializedName("humidity")
    @Expose
    var humidity: Int?,

    @SerializedName("sea_level")
    @Expose
    var seaLevel: Int?,

    @SerializedName("grnd_level")
    @Expose
    var grndLevel: Int?
)