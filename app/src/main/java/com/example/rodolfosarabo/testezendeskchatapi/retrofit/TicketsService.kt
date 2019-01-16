package com.example.rodolfosarabo.testezendeskchatapi.retrofit

import com.example.rodolfosarabo.testezendeskchatapi.models.AddComment
import com.example.rodolfosarabo.testezendeskchatapi.models.AddUser
import com.example.rodolfosarabo.testezendeskchatapi.models.NewRequest
import com.example.rodolfosarabo.testezendeskchatapi.models.UserList
import retrofit2.Call
import retrofit2.http.*

interface TicketsService {

    @POST("users.json")
    fun createUser(@Header("Authorization") auth: String, @Body user: AddUser): Call<Any>

    @GET("users.json")
    fun getUsers(@Header("Authorization") auth: String): Call<UserList>

    //REQUESTS BY ENDUSER
    @POST("requests.json")
    fun createRequest(@Header("Authorization") auth: String, @Body request: NewRequest): Call<Any>

    @GET("requests.json")
    fun listRequests(@Header("Authorization") auth: String): Call<Any>

    @PUT("requests/{id}.json")
    fun updateRequest(@Header("Authorization") auth: String, @Path("id") requestId: Long, @Body request: AddComment): Call<Any>

    @GET("requests/{id}/comments.json?")
    fun getRequestComments(@Header("Authorization") auth: String, @Path("id") requestId: Long): Call<Any>

    @GET("requests/{id}.json")
    fun getRequestById(@Header("Authorization") auth: String, @Path("id") requestId: String): Call<Any>

}