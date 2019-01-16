package com.example.rodolfosarabo.testezendeskchatapi.retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitInitializer {

    companion object {
        private const val BASE_URL = "https://companytestrodolfo.zendesk.com/api/v2/"
    }

    private val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    fun ticketsService(): TicketsService {
        return retrofit.create(TicketsService::class.java)
    }

}