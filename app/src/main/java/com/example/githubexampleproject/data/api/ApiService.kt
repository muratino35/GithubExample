package com.example.githubexampleproject.data.api

import com.example.githubexampleproject.data.model.User
import retrofit2.http.GET

interface ApiService {

    @GET("users")
    suspend fun getUsers(): List<User>

}