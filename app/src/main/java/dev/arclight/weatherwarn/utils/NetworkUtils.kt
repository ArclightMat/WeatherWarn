package dev.arclight.weatherwarn.utils

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NetworkUtils {
    companion object {
        fun getRetrofitInstance(path: String): Retrofit {
            /*
            @param path: Main path to API.
            @return: Retrofit instance for requests with GSON support.
            */
            return Retrofit.Builder()
                .baseUrl(path)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
    }
}