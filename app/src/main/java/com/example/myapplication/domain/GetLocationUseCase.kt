package com.example.myapplication.domain

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Looper
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import kotlinx.coroutines.delay
import javax.inject.Inject
import kotlin.coroutines.coroutineContext

class GetLocationUseCase @Inject constructor(): LocationCallback() {
    private val locationRequest = LocationRequest.create().apply {
        interval = 10000
        fastestInterval = 3000
        priority = Priority.PRIORITY_HIGH_ACCURACY
        maxWaitTime = 3000
    }
    private var list: ArrayList<String> = arrayListOf()

    fun getLocationResult(context: Context){
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
        LocationServices.getFusedLocationProviderClient(context)
            .requestLocationUpdates(locationRequest, object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    super.onLocationResult(locationResult)
                    LocationServices.getFusedLocationProviderClient(context)
                        .removeLocationUpdates(this)
                    if (locationResult != null && locationResult.locations.size > 0) {
                        val latestLocationIndex = locationResult.locations.size - 1
                        list.add(locationResult.locations[latestLocationIndex].latitude.toString())
                        list.add(locationResult.locations[latestLocationIndex].longitude.toString())
                    }
                }
            }, Looper.myLooper())
    }
    suspend fun getLocation(): ArrayList<String> = run {
        while (list.isEmpty()){
            delay(1000)
        }
        list
    }
}
