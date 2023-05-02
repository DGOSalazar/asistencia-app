package com.example.myapplication.sys.utils

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import androidx.core.app.ActivityCompat
import com.example.myapplication.data.models.LocationModel
import com.example.myapplication.data.models.TeamGroup
import com.example.myapplication.data.models.UserHomeDomainModel
import com.example.myapplication.data.remote.response.UserHomeResponse
import com.google.android.gms.location.*
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


class Tools @Inject constructor(private val context: Context){

    private val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

     fun isEnableGeolocation():Boolean{
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }
    @SuppressLint("MissingPermission")
    suspend fun getLocation(): LocationModel? {
        if(isLocationPermissionEnable())
            return null


        val fusedLocationClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
        val location = fusedLocationClient.lastLocation.await()

        return if (location != null) {
            LocationModel(
                latitude = location.latitude,
                longitude = location.longitude,
                altitude = location.altitude
            )
        }else
            null
    }

    private fun isLocationPermissionEnable():Boolean{
        return ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED
    }


    companion object{
        fun getTeams(res:List<UserHomeDomainModel>):ArrayList<TeamGroup>{
            var users: ArrayList<UserHomeDomainModel> = arrayListOf()
            val resTeam: ArrayList<TeamGroup> = arrayListOf(
                TeamGroup(team = "Business Analyst", users = arrayListOf(), isSelected = false),
                TeamGroup(team = "Scrum Master", users = arrayListOf(), isSelected = false),
                TeamGroup(team = "iOS Developers", users = arrayListOf(), isSelected = false),
                TeamGroup(team = "Android Developers", users = arrayListOf(), isSelected = false),
                TeamGroup(team = "Testers/QA", users = arrayListOf(), isSelected = false),
                TeamGroup(team = "Backend Developers", users = arrayListOf(), isSelected = false)
            )
        res.forEach { user ->
            when (user.position) {
                "Android Dev" -> {
                    resTeam[0].users.add(user)
                }
                "IOS Dev" -> {
                    resTeam[1].users.add(user)
                }
                "Business Analyst" -> {
                    resTeam[2].users.add(user)
                }
                "Backend Dev" -> {
                    resTeam[3].users.add(user)
                }
                "Scrum Master" -> {
                    resTeam[4].users.add(user)
                }
                "Tester/QA" -> {
                    resTeam[5].users.add(user)
                }
                else -> Unit
            }
        }
      return  resTeam
    }
    }

}