package com.example.collegeschedule.data.api

import com.example.collegeschedule.data.dto.ScheduleByDateDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ScheduleApi {
    @GET("api/schedule/group/{groupName}")
    suspend fun getSchedule(
        @Path("groupName") groupName: String,
        @Query("start") start: String,
        @Query("end") end: String
    ): List<ScheduleByDateDto>

    @GET("api/groups")
    suspend fun getAllGroups(): List<GroupDto>

    @GET("api/groups/search")
    suspend fun searchGroups(@Query("query") query: String): List<GroupDto>
}
data class GroupDto(
    val groupId: Int,
    val groupName: String,
    val course: Int,
    val specialty: String
)