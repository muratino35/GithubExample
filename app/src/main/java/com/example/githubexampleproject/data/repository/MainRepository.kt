package com.example.githubexampleproject.data.repository

import com.example.githubexampleproject.data.api.ApiHelper

class MainRepository(private val apiHelper: ApiHelper) {

    suspend fun getUsers() = apiHelper.getUsers()
}