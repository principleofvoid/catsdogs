package com.example.catsdogs

import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

private const val BASE_URL = "http://kot3.com/xim/"

object ApiRequests {

    interface CatsDogsService {
        @GET("api.php?query=cat")
        fun getCats(): Single<CatsDogsListJson>

        @GET("api.php?query=dog")
        fun getDogs(): Single<CatsDogsListJson>
    }

    private val catsDogsService = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()
        .create(CatsDogsService::class.java)

    fun requestCats() = catsDogsService.getCats().subscribeOn(Schedulers.io())
    fun requestDogs() = catsDogsService.getDogs().subscribeOn(Schedulers.io())
}