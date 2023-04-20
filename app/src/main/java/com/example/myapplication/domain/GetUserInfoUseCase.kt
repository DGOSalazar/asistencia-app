package com.example.myapplication.domain

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.myapplication.data.models.TeamGroup
import com.example.myapplication.data.remote.api.FirebaseServices
import com.example.myapplication.data.remote.response.UserHomeResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetUserInfoUseCase @Inject constructor(
    private val firebase: FirebaseServices
) {
    suspend fun userInfo(
        listEmails: ArrayList<String>,
        users: (ArrayList<UserHomeResponse>) -> Unit
    ) =
        firebase.getUserInfo(listEmails, users)

    @RequiresApi(Build.VERSION_CODES.O)
    fun emailList(day: String, mails: (ArrayList<String>) -> Unit) =
        firebase.getListUsers(day, mails)

  /*  suspend fun getUsersByTeams(): ArrayList<TeamGroup> =
        withContext(Dispatchers.IO) {
            var res: ArrayList<UserHomeDomainModel> = arrayListOf()
            val resTeam: ArrayList<TeamGroup> = arrayListOf(
                TeamGroup(team = "Business Analyst", users = arrayListOf(), isSelected = false),
                TeamGroup(team = "Scrum Master", users = arrayListOf(), isSelected = false),
                TeamGroup(team = "iOS Developers", users = arrayListOf(), isSelected = false),
                TeamGroup(team = "Android Developers", users = arrayListOf(), isSelected = false),
                TeamGroup(team = "Testers/QA", users = arrayListOf(), isSelected = false),
                TeamGroup(team = "Backend Developers", users = arrayListOf(), isSelected = false)
            )
            // while (res.isEmpty()){
            firebase.getAllUsers()?.let {
                res = it
            }


            res.forEach { user ->
                when (user.position) {
                    "Android Dev" -> {
                        resTeam[0].users.add(user)
                    }
                    "IOS" -> {
                        resTeam[1].users.add(user)
                    }
                    "Analyst" -> {
                        resTeam[2].users.add(user)
                    }
                    "Backend" -> {
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
            resTeam
        }

*/
}