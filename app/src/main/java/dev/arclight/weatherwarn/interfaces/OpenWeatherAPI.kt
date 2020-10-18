package dev.arclight.weatherwarn.interfaces

import dev.arclight.weatherwarn.models.WeatherData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenWeatherAPI {

    @GET("data/2.5/weather?")
    fun getCurrentWeather(
        @Query("lat") latitude: String,
        @Query("lon") longitude: String,
        @Query("appid") apiKey: String,
        @Query("lang") language: String
    ): Call<WeatherData>
}