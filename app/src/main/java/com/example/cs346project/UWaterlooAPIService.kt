package com.example.cs346project

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

const val BASE_URL = "https://openapi.data.uwaterloo.ca/v3/"

interface APIService {

    @GET("Courses/1239")
    suspend fun getCourseInfo(): List<CourseInfoData>

    @GET("ClassSchedules/1239/{subject}/{courseNumber}")
    suspend fun getClassScheduleInfo(
        @Path("subject") subject: String,
        @Path("courseNumber") courseNumber: String
    ): List<ClassScheduleData>

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