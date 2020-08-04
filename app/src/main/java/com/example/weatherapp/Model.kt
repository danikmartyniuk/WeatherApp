package com.example.weatherapp

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.LocationServices
import com.google.gson.GsonBuilder
import okhttp3.*
import java.io.IOException
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

class Model {

    private val API = "fd46072fbce971220c44c51cbe3b3a0d"

    fun getInfoFromApi(
        context: Context,
        weatherImg: ImageView,
        locationTv: TextView,
        weatherTv: TextView,
        humidityTv: TextView,
        cloudinessTv: TextView,
        pressureTv: TextView,
        windSpeedTv: TextView,
        windDirection: TextView
    ) {
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

        fusedLocationProviderClient.lastLocation.addOnSuccessListener { location: Location? ->
            if (location != null) {
                val url =
                    "https://api.openweathermap.org/data/2.5/forecast?lat=${location.latitude}&lon=${location.longitude}&appid=${API}"
                val request = Request.Builder().url(url).build()
                val client = OkHttpClient()
                client.newCall(request).enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        println("Failed to execute request")
                    }

                    @SuppressLint("SetTextI18n")
                    override fun onResponse(call: Call, response: Response) {
                        val body = response.body()?.string()
                        val forecasts = GsonBuilder().create().fromJson(body, Forecast::class.java)
                        Handler(Looper.getMainLooper()).post {
                            setImageByDescription(forecasts.list[0].weather[0].description.capitalize(), weatherImg)
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

    fun shareWeather () {

    }

    fun getForecastForFiveDays(context: Context,
                               day1tv: TextView, forecast1Rv: RecyclerView, day2tv: TextView, forecast2Rv: RecyclerView,
                               day3tv: TextView, forecast3Rv: RecyclerView, day4tv: TextView, forecast4Rv: RecyclerView,
                               day5tv: TextView, forecast5Rv: RecyclerView) {
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

        fusedLocationProviderClient.lastLocation.addOnSuccessListener { location: Location? ->
            if (location != null) {
                val url =
                    "https://api.openweathermap.org/data/2.5/forecast?lat=${location.latitude}&lon=${location.longitude}&appid=${API}"
                val request = Request.Builder().url(url).build()
                val client = OkHttpClient()
                client.newCall(request).enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        println("Failed to execute request")
                    }

                    @SuppressLint("SetTextI18n", "SimpleDateFormat")
                    override fun onResponse(call: Call, response: Response) {
                        val body = response.body()?.string()
                        val forecasts = GsonBuilder().create().fromJson(body, Forecast::class.java)
                        Handler(Looper.getMainLooper()).post {
                            val listNamesOfDays = listOf(day1tv, day2tv, day3tv, day4tv, day5tv)
                            val listTimes: MutableList<MutableList<String>> = mutableListOf(mutableListOf(), mutableListOf(), mutableListOf(), mutableListOf(), mutableListOf())
                            val listConditions: MutableList<MutableList<String>> = mutableListOf(mutableListOf(), mutableListOf(), mutableListOf(), mutableListOf(), mutableListOf())
                            val listDegrees: MutableList<MutableList<Double>> = mutableListOf(mutableListOf(), mutableListOf(), mutableListOf(), mutableListOf(), mutableListOf())
                            var currentDate = forecasts.list[0].dt_txt.substring(0, forecasts.list[0].dt_txt.indexOf(" "))
                            var dayNumber = 0
                            for (times in forecasts.list) {
                                val dateInApi = times.dt_txt.substring(0, times.dt_txt.indexOf(" "))
                                if (currentDate == dateInApi) {
                                    listTimes[dayNumber].add(times.dt_txt.substring(times.dt_txt.indexOf(" "), times.dt_txt.length - 3))
                                    listConditions[dayNumber].add(times.weather[0].description.capitalize())
                                    listDegrees[dayNumber].add(times.main.feels_like)
                                } else {
                                    listNamesOfDays[dayNumber].text = getDayString(SimpleDateFormat("yyyy-mm-dd").parse(currentDate), Locale.ENGLISH)
                                    currentDate = dateInApi
                                    dayNumber++
                                    if (dayNumber == 5) break
                                }
                                forecast1Rv.adapter = Adapter(listTimes[0], listConditions[0], listDegrees[0])
                                forecast2Rv.adapter = Adapter(listTimes[1], listConditions[1], listDegrees[1])
                                forecast3Rv.adapter = Adapter(listTimes[2], listConditions[2], listDegrees[2])
                                forecast4Rv.adapter = Adapter(listTimes[3], listConditions[3], listDegrees[3])
                                forecast5Rv.adapter = Adapter(listTimes[4], listConditions[4], listDegrees[4])
                            }
                        }
                    }
                })
            }
        }
    }

    fun getDayString(date: Date, locale: Locale): String {
        val formatter: DateFormat = SimpleDateFormat("EEEE", locale)
        return formatter.format(date)
    }

    fun setImageByDescription (description: String, imgView: ImageView) {
        when (description) {
            "Sunny" -> imgView.setImageResource(R.drawable.sunny)
            "Scattered clouds" -> imgView.setImageResource(R.drawable.scattered_clouds)
            "Few clouds" -> imgView.setImageResource(R.drawable.few_clouds)
            "Clear sky" -> imgView.setImageResource(R.drawable.clear_sky)
            "Overcast clouds" -> imgView.setImageResource(R.drawable.overcast_clouds)
            "Broken clouds" -> imgView.setImageResource(R.drawable.dark_clouds)
            "Light rain" -> imgView.setImageResource(R.drawable.light_rain)
            "Moderate rain" -> imgView.setImageResource(R.drawable.moderate_rain)
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
class Day(val main: Main, val wind: Wind, val weather: Array<Weather>, val clouds: Cloud, val dt_txt: String)
class Main (val feels_like: Double, val pressure: Int, val humidity: Int)
class Wind (val speed: Double, val deg: Int)
class Weather (val description: String)
class Cloud (val all: Int)
class City (val name: String, val country: String)

class Adapter (private val times: List<String>, private val conditions: List<String>, private val weatherDegree: List<Double>): RecyclerView.Adapter<Adapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_item_view, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return times.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.weatherImg?.let { Model().setImageByDescription(conditions[position], it) }
        holder.timeTv?.text = times[position]
        holder.weatherTv?.text = conditions[position]
        holder.degreesTv?.text = "${(weatherDegree[position] - 273.15).roundToInt()}°C"
    }

    class ViewHolder (itemView: View): RecyclerView.ViewHolder(itemView) {
        var weatherImg: ImageView? = null
        var timeTv : TextView? = null
        var weatherTv : TextView? = null
        var degreesTv : TextView? = null
        init {
            weatherImg = itemView.findViewById(R.id.forecast_imgview)
            timeTv = itemView.findViewById(R.id.time)
            weatherTv = itemView.findViewById(R.id.weather_condition)
            degreesTv = itemView.findViewById(R.id.degrees)
        }
    }

}