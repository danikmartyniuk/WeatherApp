package com.example.weatherapp

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView


class TodayActivity : AppCompatActivity(), Presenter.View {

    lateinit var presenter: Presenter
    lateinit var weatherImg: ImageView
    lateinit var locationTv: TextView
    lateinit var currentWeatherTv: TextView
    lateinit var humidityTv: TextView
    lateinit var cloudinessTv: TextView
    lateinit var pressureTv: TextView
    lateinit var windSpeedTv: TextView
    lateinit var windDirection: TextView
    lateinit var shareBtn: Button

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.my_toolbar)
        setSupportActionBar(toolbar)
        val bottomNav: BottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNav.selectedItemId = R.id.today_nav
        bottomNav.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        val observableObject = ObservableObject(PrintingTextChangedListener(), this@TodayActivity)
        observableObject.network = isOnline(this@TodayActivity)
        observableObject.location = isLocationEnabled(this@TodayActivity)

        presenter = Presenter(this@TodayActivity)

        weatherImg = findViewById(R.id.current_weather_img)
        locationTv = findViewById(R.id.current_location)
        currentWeatherTv = findViewById(R.id.current_weather_tv)
        humidityTv = findViewById(R.id.humidity_tv)
        cloudinessTv = findViewById(R.id.cloudiness)
        pressureTv = findViewById(R.id.pressure_tv)
        windSpeedTv = findViewById(R.id.wind_speed_tv)
        windDirection = findViewById(R.id.wind_direction_tv)
        if (ContextCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this@TodayActivity,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                1
            )
        } else {
            setParameters(
                this@TodayActivity,
                weatherImg,
                locationTv,
                currentWeatherTv,
                humidityTv,
                cloudinessTv,
                pressureTv,
                windSpeedTv,
                windDirection
            )
        }

        shareBtn = findViewById(R.id.share)
        shareBtn.setOnClickListener {
            onShareClick(this@TodayActivity)
        }

        val customView: View = findViewById(R.id.custom_view)
        customView.layoutParams.height = 110
        customView.requestLayout()
    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.forecast_nav -> {
                val intent = Intent(this, ForecastActivity::class.java)
                startActivity(intent)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun setDataToRecyclerViews(
        context: Context,
        day1tv: TextView,
        forecast1Rv: RecyclerView,
        day2tv: TextView,
        forecast2Rv: RecyclerView,
        day3tv: TextView,
        forecast3Rv: RecyclerView,
        day4tv: TextView,
        forecast4Rv: RecyclerView,
        day5tv: TextView,
        forecast5Rv: RecyclerView
    ) {

    }

    override fun setParameters(context: Context, weatherImg: ImageView, locationTv: TextView, weatherTv: TextView, humidityTv: TextView, cloudinessTv: TextView, pressureTv: TextView, windSpeedTv: TextView, windDirection: TextView) {
        presenter.getInfo(context, weatherImg, locationTv, weatherTv, humidityTv, cloudinessTv, pressureTv, windSpeedTv, windDirection)
    }

    override fun onShareClick(context: Context) {
        presenter.share(context)
    }

}