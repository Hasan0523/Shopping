package com.example.shopping.networking

import com.example.shopping.model.Login
import com.example.shopping.model.ProductData
import com.example.shopping.model.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface APIService {
    @GET("/products")
    fun getAll(): Call<ProductData>

    @GET("/products/categories")
    fun getCategories(): Call<List<String>>

    @GET("/products/category/{category}")
    fun getByCategory(@Path("category") category : String): Call<ProductData>


    @GET("/products/search")
    fun search(@Query("q") query : String): Call<ProductData>


    @POST("/auth/login")
    fun search(@Body login: Login): Call<User>

    @POST("/auth/login")
    fun login(@Body login: Login): Call<User>
}