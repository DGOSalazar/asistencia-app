package com.example.myapplication.domain

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Looper
import androidx.core.app.ActivityCompat
import com.example.myapplication.data.models.AssistConfirm
import com.example.myapplication.data.models.UserOk
import com.example.myapplication.data.remote.api.FirebaseServices
import com.google.android.gms.location.*
import kotlinx.coroutines.delay
import javax.inject.Inject

class GetLocationUseCase @Inject constructor(
    private val firebaseServices: FirebaseServices
): LocationCallback() {

    private var isConfirm: Boolean? = null
    private lateinit var today : String
    private var userOk: UserOk = UserOk()

    private val locationRequest = LocationRequest.create().apply {
        interval = 10000
        fastestInterval = 3000
        priority = Priority.PRIORITY_HIGH_ACCURACY
        maxWaitTime = 3000
    }

    fun checkConfirmStatus(actualDay: String, email: String){
        today = actualDay
        userOk.email = email
        firebaseServices.consultUserConfirmationAssist(actualDay,email){
            //TODO(Validar lista no contenga el email para cambiar el valor de bandera )
        }
    }
    suspend fun getConfirm(): Boolean{
        while (isConfirm==null){
            delay(1000)
        }
        return isConfirm as Boolean
    }

    suspend fun getLocationResult(context: Context){
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ){
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
                        userOk.lat = locationResult.locations[latestLocationIndex].latitude
                        userOk.lon = locationResult.locations[latestLocationIndex].longitude
                    }
                }
            }, Looper.myLooper())
        setDayWithUsersLocation()
    }
    private suspend fun setDayWithUsersLocation() {
        while (userOk.lat.isNaN() && userOk.lon.isNaN()){
            delay(1000)
        }
        firebaseServices.consultUserConfirmationAssist(today,userOk.email){
            if (it.isNotEmpty()){
                var list = it[0].users

                list.add(
                    UserOk(
                        email = userOk.email,
                        lat = userOk.lat,
                        lon = userOk.lon,
                        isInOffice = isOnOffice()
                    )
                )
                firebaseServices.registerUserConfirmationAssist(
                    AssistConfirm(
                        day = today,
                        users =  list
                    )
                )
            }
            else{
                firebaseServices.registerUserConfirmationAssist(
                    (AssistConfirm(day = today,
                            arrayListOf(UserOk(
                                email = userOk.email,
                                lat = userOk.lat,
                                lon = userOk.lon,
                                isInOffice = isOnOffice())
                                )
                            )
                        )
                )
            }
        }
    }

    private fun isOnOffice(): Boolean {
        return userOk.lat in 20.68107..20.68307 && userOk.lon in -103.38650..-103.38250
    }
}
