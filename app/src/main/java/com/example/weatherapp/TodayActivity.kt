package com.example.weatherapp

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


class TodayActivity : AppCompatActivity(), Presenter.View {

    lateinit var presenter: Presenter
    lateinit var locationTv: TextView
    lateinit var currentWeatherTv: TextView
    lateinit var humidityTv: TextView
    lateinit var cloudinessTv: TextView
    lateinit var pressureTv: TextView
    lateinit var windSpeedTv: TextView
    lateinit var windDirection: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.my_toolbar)
        setSupportActionBar(toolbar)
        val bottomNav: BottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNav.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        presenter = Presenter(this@TodayActivity)

        locationTv = findViewById(R.id.current_location)
        currentWeatherTv = findViewById(R.id.current_weather_tv)
        humidityTv = findViewById(R.id.humidity_tv)
        cloudinessTv = findViewById(R.id.cloudiness)
        pressureTv = findViewById(R.id.pressure_tv)
        windSpeedTv = findViewById(R.id.wind_speed_tv)
        windDirection = findViewById(R.id.wind_direction_tv)
        if (ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this@TodayActivity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
        } else {
            setParameters(this@TodayActivity, locationTv, currentWeatherTv, humidityTv, cloudinessTv, pressureTv, windSpeedTv, windDirection)
        }

    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.forecast -> {
                val intent = Intent(this, ForecastActivity::class.java)
                startActivity(intent)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun setParameters(context: Context, locationTv: TextView, weatherTv: TextView, humidityTv: TextView, cloudinessTv: TextView, pressureTv: TextView, windSpeedTv: TextView, windDirection: TextView) {
        presenter.getInfo(context, locationTv, weatherTv, humidityTv, cloudinessTv, pressureTv, windSpeedTv, windDirection)
    }

}