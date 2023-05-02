package com.example.myapplication.domain

import com.example.myapplication.core.utils.Status
import com.example.myapplication.core.utils.statusNetwork.Resource2
import com.example.myapplication.data.mappers.UserHomeMapper
import com.example.myapplication.data.models.TeamGroup
import com.example.myapplication.data.models.UserHomeDomainModel
import com.example.myapplication.data.remote.webDS.TeamWebDS
import com.example.myapplication.sys.utils.Tools
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TeamRepository @Inject constructor(private val webDS: TeamWebDS) {
    suspend fun getUsersByTeams(): Flow<Resource2<ArrayList<TeamGroup>>> =
        webDS.getAllUsersTeams().map {
            if (it.status == Status.SUCCESS) {
                val list = arrayListOf<UserHomeDomainModel>()
                it.data?.forEach {
                    list.add(UserHomeMapper().map(it))
                }
                val teams = Tools.getTeams(list)
                Resource2.success(teams)
            }
            else
                Resource2.error(it.message)
        }
}