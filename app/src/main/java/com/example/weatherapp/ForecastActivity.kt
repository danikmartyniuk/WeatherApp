package com.example.weatherapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView

class ForecastActivity : AppCompatActivity(), Presenter.View {

    lateinit var presenter: Presenter

    lateinit var recyclerView1: RecyclerView
    lateinit var day1Tv: TextView
    lateinit var recyclerView2: RecyclerView
    lateinit var day2Tv: TextView
    lateinit var recyclerView3: RecyclerView
    lateinit var day3Tv: TextView
    lateinit var recyclerView4: RecyclerView
    lateinit var day4Tv: TextView
    lateinit var recyclerView5: RecyclerView
    lateinit var day5Tv: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forecast)
        val toolbar: Toolbar = findViewById(R.id.my_toolbar)
        setSupportActionBar(toolbar)
        val bottomNav: BottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNav.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        presenter = Presenter(this@ForecastActivity)

        recyclerView1 = findViewById(R.id.forecast1)
        recyclerView1.layoutManager = LinearLayoutManager(this)
        day1Tv = findViewById(R.id.day1)

        recyclerView2 = findViewById(R.id.forecast2)
        recyclerView2.layoutManager = LinearLayoutManager(this)
        day2Tv = findViewById(R.id.day2)

        recyclerView3 = findViewById(R.id.forecast3)
        recyclerView3.layoutManager = LinearLayoutManager(this)
        day3Tv = findViewById(R.id.day3)

        recyclerView4 = findViewById(R.id.forecast4)
        recyclerView4.layoutManager = LinearLayoutManager(this)
        day4Tv = findViewById(R.id.day4)

        recyclerView5 = findViewById(R.id.forecast5)
        recyclerView5.layoutManager = LinearLayoutManager(this)
        day5Tv = findViewById(R.id.day5)

        setDataToRecyclerViews(this, day1Tv, recyclerView1, day2Tv, recyclerView2, day3Tv, recyclerView3, day4Tv, recyclerView4, day5Tv, recyclerView5)
    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.today_nav -> {
                val intent = Intent(this, TodayActivity::class.java)
                startActivity(intent)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun setDataToRecyclerViews(
        context: Context,
        day1tv: TextView, forecast1Rv: RecyclerView, day2tv: TextView, forecast2Rv: RecyclerView,
        day3tv: TextView, forecast3Rv: RecyclerView, day4tv: TextView, forecast4Rv: RecyclerView,
        day5tv: TextView, forecast5Rv: RecyclerView
    ) {
        presenter.getDataForFiveDays(context, day1Tv, recyclerView1, day2Tv, recyclerView2, day3Tv, recyclerView3, day4Tv, recyclerView4, day5Tv, recyclerView5)
    }

    override fun setParameters(
        context: Context,
        locationTv: TextView,
        weatherTv: TextView,
        humidityTv: TextView,
        cloudinessTv: TextView,
        pressureTv: TextView,
        windSpeedTv: TextView,
        windDirection: TextView
    ) {

    }
}