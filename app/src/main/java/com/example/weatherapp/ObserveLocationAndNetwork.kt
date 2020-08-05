package com.example.weatherapp

import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.location.LocationManagerCompat
import kotlin.properties.Delegates

interface ValueChangeListener {
    fun onNetworkDisabled (isEnabled: Boolean, context: Context)
    fun onLocationDisabled (isEnabled: Boolean, context: Context)
}

class PrintingTextChangedListener: ValueChangeListener {

    override fun onNetworkDisabled(isEnabled: Boolean, context: Context) {
        if (!isEnabled) {
            println("Now network is not working")
            val intent = Intent(context, NoInternetActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onLocationDisabled(isEnabled: Boolean, context: Context) {
        if (!isEnabled) {
            println("Now location doesn't work")
            val intent = Intent(context, NoInternetActivity::class.java)
            context.startActivity(intent)
        }
    }
}

class ObservableObject (listener: ValueChangeListener, context: Context) {
    var network: Boolean by Delegates.observable(
        initialValue = true,
        onChange = {
                _, _, newValue ->  listener.onNetworkDisabled(newValue, context)
        }
    )

    var location: Boolean by Delegates.observable(
        initialValue = true,
        onChange = {
                _, _, newValue ->  listener.onLocationDisabled(newValue, context)
        }
    )
}

@RequiresApi(Build.VERSION_CODES.M)
fun isOnline(context: Context): Boolean {
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val capabilities =
        connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
    if (capabilities != null) {
        if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
            Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
            return true
        } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
            Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
            return true
        } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
            Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
            return true
        }
    }
    return false
}

fun isLocationEnabled(context: Context): Boolean {
    val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    return LocationManagerCompat.isLocationEnabled(locationManager)
}