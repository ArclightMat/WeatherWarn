package dev.arclight.weatherwarn.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import dev.arclight.weatherwarn.R
import dev.arclight.weatherwarn.interfaces.OpenWeatherAPI
import dev.arclight.weatherwarn.models.WeatherData
import dev.arclight.weatherwarn.utils.NetworkUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

const val apiUrl: String = "https://api.openweathermap.org"
const val apiKey: String = "" // Insert OpenWeatherMap API Key here
const val lang: String = "pt_BR" // TODO: Get this value automatically.

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loadWeatherData()
    }

    fun loadWeatherData() {
        val weatherClient = NetworkUtils.getRetrofitInstance(apiUrl)
        val endpoint = weatherClient.create(OpenWeatherAPI::class.java)
        val callback = endpoint.getCurrentWeather("0.0", "0.0", apiKey, lang)

        callback.enqueue(object : Callback<WeatherData> {
            override fun onFailure(call: Call<WeatherData>, t: Throwable) {
                Toast.makeText(baseContext, t.message, Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<WeatherData>,response: Response<WeatherData>) {
                TODO("Implement UI")
            }
        })
    }
}