package com.example.weatherapp

import android.content.Context
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class Presenter {

    private var model: Model
    private var view: View

    constructor (view: View) {
        this.model = Model()
        this.view = view
    }

    fun getInfo (context: Context, locationTv: TextView, weatherTv: TextView, humidityTv: TextView, cloudinessTv: TextView, pressureTv: TextView, windSpeedTv: TextView, windDirection: TextView) {
        return model.getInfoFromApi(context, locationTv, weatherTv, humidityTv, cloudinessTv, pressureTv, windSpeedTv, windDirection)
    }

    fun getDataForFiveDays (context: Context, day1tv: TextView, forecast1Rv: RecyclerView, day2tv: TextView, forecast2Rv: RecyclerView,
                            day3tv: TextView, forecast3Rv: RecyclerView, day4tv: TextView, forecast4Rv: RecyclerView,
                            day5tv: TextView, forecast5Rv: RecyclerView) {
        model.getForecastForFiveDays(context, day1tv, forecast1Rv, day2tv, forecast2Rv, day3tv, forecast3Rv, day4tv, forecast4Rv, day5tv, forecast5Rv)
    }

    interface View {
        fun setDataToRecyclerViews (context: Context, day1tv: TextView, forecast1Rv: RecyclerView, day2tv: TextView, forecast2Rv: RecyclerView,
                                    day3tv: TextView, forecast3Rv: RecyclerView, day4tv: TextView, forecast4Rv: RecyclerView,
                                    day5tv: TextView, forecast5Rv: RecyclerView)
        fun setParameters(context: Context, locationTv: TextView, weatherTv: TextView, humidityTv: TextView, cloudinessTv: TextView, pressureTv: TextView, windSpeedTv: TextView, windDirection: TextView)
    }
}