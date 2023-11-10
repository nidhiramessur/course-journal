package com.example.cs346project

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

const val BASE_URL = "https://openapi.data.uwaterloo.ca/v3/"

interface APIService {

    @GET("Courses/1239")
    suspend fun getCourseInfoDataFromGET(): List<CourseInfoData>

    @GET("ClassSchedules/1239/cs/346")
    suspend fun getCourseInfoAPIDataFromGET(): List<CourseInfoAPIData>

    companion object {
        private val okHttpClient = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val originalRequest = chain.request()
                val newRequest = originalRequest.newBuilder()
                    .header("X-API-KEY", "7E9C16B5349F4D898AB68BC33310BCAA")
                    .build()
                chain.proceed(newRequest)
            }
            .build()

        val apiService: APIService by lazy {
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(APIService::class.java)
        }
    }
}