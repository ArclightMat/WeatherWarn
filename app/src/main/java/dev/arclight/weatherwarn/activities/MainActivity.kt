package dev.arclight.weatherwarn.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import dev.arclight.weatherwarn.R
import dev.arclight.weatherwarn.interfaces.OpenWeatherAPI
import dev.arclight.weatherwarn.models.WeatherData
import dev.arclight.weatherwarn.utils.NetworkUtils
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

const val apiUrl: String = "https://api.openweathermap.org"
const val unit: String = "metric"
const val apiKey: String = "" // Insert OpenWeatherMap API Key here
const val lang: String = "pt_BR" // TODO: Get this value automatically.

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        weatherProgress.visibility = View.VISIBLE
        loadWeatherData()
    }

    fun loadWeatherData() {
        val weatherClient = NetworkUtils.getRetrofitInstance(apiUrl)
        val endpoint = weatherClient.create(OpenWeatherAPI::class.java)
        val callback = endpoint.getCurrentWeather("", "", apiKey, unit, lang)

        callback.enqueue(object : Callback<WeatherData> {
            override fun onFailure(call: Call<WeatherData>, t: Throwable) {
                Toast.makeText(baseContext, t.message, Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<WeatherData>,response: Response<WeatherData>) {
                weatherProgress.visibility = View.INVISIBLE
                // TODO: Use placeholder strings
                // TODO: Add last updated
                name.text = response.body()?.name
                response.body()?.weatherData?.forEach {
                    todayDescription.text = it.description
                    // TODO: Main/Icon
                }
                // Main
                todayTemp.text = response.body()?.main?.temp.toString()
                todayHumidity.text = response.body()?.main?.humidity.toString()
                todayPressure.text = response.body()?.main?.pressure.toString()
                // TODO: Add FeelsLike, TempMin, TempMax
                // Sys
                todaySunrise.text = response.body()?.sys?.sunrise.toString() // TODO: Convert Unix time
                todaySunset.text = response.body()?.sys?.sunrise.toString() // TODO: Convert Unix time
                // Wind
                todayWind.text = response.body()?.wind?.speed.toString()

            }
        })
    }

    fun getLocation() {
        TODO("Permission Request + Latitude/Longitude")
    }

    fun refreshData() {
        TODO("Needs UI button")
        getLocation()
        loadWeatherData()
    }
}