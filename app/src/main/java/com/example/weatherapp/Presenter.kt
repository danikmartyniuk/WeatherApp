package com.example.weatherapp

import android.content.Context
import android.widget.TextView

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

    interface View {
        fun setParameters(context: Context, locationTv: TextView, weatherTv: TextView, humidityTv: TextView, cloudinessTv: TextView, pressureTv: TextView, windSpeedTv: TextView, windDirection: TextView)
    }

}