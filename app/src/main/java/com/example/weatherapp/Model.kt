package com.example.weatherapp

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Handler
import android.os.Looper
import android.widget.TextView
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.LocationServices
import com.google.gson.GsonBuilder
import okhttp3.*
import java.io.FileDescriptor
import java.io.IOException
import java.util.*
import kotlin.math.roundToInt

class Model  {

    private val API = "fd46072fbce971220c44c51cbe3b3a0d"

    fun getInfoFromApi (context: Context, locationTv: TextView, weatherTv: TextView, humidityTv: TextView, cloudinessTv: TextView, pressureTv: TextView, windSpeedTv: TextView, windDirection: TextView) {
        val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        fusedLocationProviderClient.lastLocation.addOnSuccessListener { location : Location? ->
            if (location != null) {
                val url = "https://api.openweathermap.org/data/2.5/forecast?lat=${location.latitude}&lon=${location.longitude}&appid=${API}"
                val request = Request.Builder().url(url).build()
                val client = OkHttpClient()
                client.newCall(request).enqueue(object: Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        println("Failed to execute request")
                    }

                    @SuppressLint("SetTextI18n")
                    override fun onResponse(call: Call, response: Response) {
                        val body = response.body()?.string()
                        val forecasts = GsonBuilder().create().fromJson(body, Forecast::class.java)
                        Handler(Looper.getMainLooper()).post {
                            locationTv.text = "${forecasts.city.name}, ${forecasts.city.country}"
                            weatherTv.text = "${(forecasts.list[0].main.feels_like - 273.15).roundToInt()}°C | ${forecasts.list[0].weather[0].description.capitalize()}"
                            humidityTv.text = "${forecasts.list[0].main.humidity}%"
                            cloudinessTv.text = "${forecasts.list[0].clouds.all}%"
                            pressureTv.text = "${forecasts.list[0].main.pressure} hpa"
                            windSpeedTv.text = "${forecasts.list[0].wind.speed * 3.6} km/h"
                            val dir: Int = forecasts.list[0].wind.deg
                            when {
                                dir < 45 -> windDirection.text = "N"
                                dir < 90 -> windDirection.text = "NE"
                                dir < 135 -> windDirection.text = "E"
                                dir < 180 -> windDirection.text = "SE"
                                dir < 225 -> windDirection.text = "S"
                                dir < 270 -> windDirection.text = "SW"
                                dir < 315 -> windDirection.text = "W"
                                dir < 360 -> windDirection.text = "NW"
                            }
                        }
                    }
                })
            }
        }
    }
}

//    Example of response
//    {"dt":1596402000,"main":
//    {"temp":286.99,"feels_like":286.1,"temp_min":286.83,"temp_max":286.99,"pressure":1014,"sea_level":1015,"grnd_level":992,"humidity":79,"temp_kf":0.16},
//    "weather":[{"id":804,"main":"Clouds","description":"overcast clouds","icon":"04n"}],
//    "clouds":{"all":93},"wind":{"speed":1.44,"deg":202},
//    "visibility":10000,"pop":0,"sys":{"pod":"n"},
//    "dt_txt":"2020-08-02 21:00:00"}

class Forecast (val list: Array<Day>, val city: City)
class Day(val main: Main, val wind: Wind, val weather: Array<Weather>, val clouds: Cloud)
class Main (val feels_like: Double, val pressure: Int, val humidity: Int)
class Wind (val speed: Double, val deg: Int)
class Weather (val description: String)
class Cloud (val all: Int)
class City (val name: String, val country: String)