package dev.arclight.weatherwarn.activities

import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.android.gms.location.*
import dev.arclight.weatherwarn.R
import dev.arclight.weatherwarn.interfaces.OpenWeatherAPI
import dev.arclight.weatherwarn.models.WeatherData
import dev.arclight.weatherwarn.utils.NetworkUtils
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

const val apiUrl: String = "https://api.openweathermap.org"
const val unit: String = "metric"
const val apiKey: String = "d812964462fe23a175e9854e19876a78" // Insert OpenWeatherMap API Key here
const val lang: String = "pt_BR"
const val LOCATION_CODE = 42
private lateinit var locationCallback: LocationCallback
private lateinit var locationRequest: LocationRequest
private lateinit var fusedLocationClient: FusedLocationProviderClient

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        locationRequest = LocationRequest.create().apply {
            interval = 3000
            fastestInterval = 1000
            priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
        }
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult?) {
                result ?: return
                loadWeatherData(result.lastLocation.latitude.toString(), result.lastLocation.longitude.toString())
                fusedLocationClient.removeLocationUpdates(locationCallback)
            }
        }
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        getLocationAndWeather(false)
    }

    override fun onResume() {
        super.onResume()
        getLocationAndWeather(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Load custom Action Buttons
        val inflater = menuInflater
        inflater.inflate(R.menu.genericmenu, menu)
        return true
    }

    override fun onPause() {
        super.onPause()
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.action_refresh -> {
            Toast.makeText(baseContext, getString(R.string.menu_refresh), Toast.LENGTH_SHORT).show()
            getLocationAndWeather(true)
            true
        }
        else -> {
            // Invoke superclass to handle non-recognized actions
            super.onOptionsItemSelected(item)
        }
    }

    private fun loadWeatherData(lat: String, lon: String) {
        val weatherClient = NetworkUtils.getRetrofitInstance(apiUrl)
        val endpoint = weatherClient.create(OpenWeatherAPI::class.java)
        val callback = endpoint.getCurrentWeather(lat, lon, apiKey, unit, lang)

        callback.enqueue(object : Callback<WeatherData> {
            override fun onFailure(call: Call<WeatherData>, t: Throwable) {
                Toast.makeText(baseContext, t.message, Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<WeatherData>, response: Response<WeatherData>) {
                locationName.text = response.body()?.name
                lastUpdated.text = getString(R.string.last_updated, SimpleDateFormat("HH:mm", Locale.ENGLISH).format(Date(
                    (response.body()?.dt?.toLong() ?: 0) *1000)))
                response.body()?.weatherData?.forEach {
                    todayDescription.text = it.description
                }
                // Main
                todayTemp.text = getString(R.string.temp, response.body()?.main?.temp)
                todayHumidity.text = getString(R.string.humd, response.body()?.main?.humidity)
                todayPressure.text = getString(R.string.pressure, response.body()?.main?.pressure)
                todayFeelsLike.text = getString(R.string.feels_like, response.body()?.main?.feelsLike)
                todayTempMin.text = getString(R.string.temp_min,response.body()?.main?.tempMin)
                todayTempMax.text = getString(R.string.temp_max,response.body()?.main?.tempMax)
                // Sys
                todaySunrise.text = SimpleDateFormat("HH:mm", Locale.ENGLISH).format(Date(
                    (response.body()?.sys?.sunrise?.toLong() ?: 0) *1000))
                todaySunset.text = SimpleDateFormat("HH:mm", Locale.ENGLISH).format(Date(
                    (response.body()?.sys?.sunset?.toLong() ?: 0) *1000))
                // Wind
                todayWind.text = getString(R.string.wind, response.body()?.wind?.speed)
            }
        })
    }

    private fun getLocationAndWeather(refresh: Boolean) {
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_DENIED
        ) {
            shouldShowRequestPermissionRationale(getString(R.string.permission_location))
            requestPermissions(
                arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION),
                LOCATION_CODE
            )
            return getLocationAndWeather(false)
        } else {
            if (refresh) {
                fusedLocationClient.requestLocationUpdates(
                    locationRequest,
                    locationCallback,
                    Looper.getMainLooper()
                )
            } else {
                fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                    loadWeatherData(location?.latitude.toString(), location?.longitude.toString())
                }
            }
        }
    }
}