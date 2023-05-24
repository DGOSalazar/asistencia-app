package com.example.myapplication.domain

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.myapplication.core.utils.Status
import com.example.myapplication.core.utils.statusNetwork.Resource2
import com.example.myapplication.data.mappers.DayCollectionMapper
import com.example.myapplication.data.mappers.UserHomeMapper
import com.example.myapplication.data.models.DayCollection
import com.example.myapplication.data.models.HomeInitialData
import com.example.myapplication.data.models.UserHomeDomainModel
import com.example.myapplication.data.remote.response.DayCollectionResponse
import com.example.myapplication.data.remote.response.UserHomeResponse
import com.example.myapplication.data.remote.webDS.UserHomeWebDS
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class UserHomeRepository @Inject constructor(
    private val webDS: UserHomeWebDS
) {

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun getHomeInitialData(email: String, date: String) =
        flow {
            var userData: Resource2<UserHomeDomainModel> = Resource2.success(UserHomeDomainModel())
            var allUserList: Resource2<List<UserHomeResponse>> = Resource2.success(listOf())
            var attendanceUserList: Resource2<List<UserHomeResponse>> = Resource2.success(listOf())
            var dayCollectionModels: Resource2<List<DayCollection>> = Resource2.success(listOf())

            val allUsers = webDS.getInfoAllUsers().single().single()
            if(allUsers.status == Status.SUCCESS ){
                val users = arrayListOf<UserHomeResponse>()
                allUsers.data?.forEach { user ->
                    if(user.email == email)
                        userData = Resource2.success(UserHomeMapper().map(user))
                    users.add(user)
                }
                allUserList = Resource2.success(users)
            }else {
                allUserList = Resource2.error(allUsers.message)
                userData = Resource2.error(allUsers.message)
            }

            val daysCollection = webDS.getDays().single().single()
            attendanceUserList = if(daysCollection.status == Status.SUCCESS){

                val currentDay:List<DayCollectionResponse> = daysCollection.data?.filter { day -> day.currentDay == date }?: emptyList()
                dayCollectionModels = Resource2.success(daysCollection.data?.map { day -> DayCollectionMapper().map(day) })
                if (allUserList.status == Status.SUCCESS){
                    val filterList =
                        if (currentDay.isNotEmpty())
                            allUserList.data?.filter { user -> user.email in currentDay[0].email!! }
                        else
                            emptyList()
                    Resource2.success(filterList)
                }else
                    Resource2.error(allUserList.message)
            }else{
                Resource2.error(daysCollection.message)
            }

            emit(Resource2.success(
                HomeInitialData(
                    remoteDays = dayCollectionModels,
                    userList =  attendanceUserList,
                    userData = userData
                )
            ))
        }.catch { error ->
            error.message?.let {
                emit(Resource2.error(it))
            }
        }


}