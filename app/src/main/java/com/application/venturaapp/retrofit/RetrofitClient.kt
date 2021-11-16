package com.application.venturaapp.retrofit

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.util.Log
import com.application.venturaapp.api.ApiService
import com.application.venturaapp.helper.Constants
import com.application.venturaapp.retrofit.UnsafeOkHttpClient.Companion.getUnsafeOkHttpClient
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.Cache
import okhttp3.Credentials
import okhttp3.Interceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Exception
import java.util.concurrent.TimeUnit


object RetrofitClient {

    private var apiService: ApiService? = null
    private var apiServiceFor: ApiService? = null
    val cacheSize = (5 * 1024 * 1024).toLong()
   // var httpCacheDirectory: File = File(getCacheDir(), "responses")


   // val myCache = Cache(getCacheDir(), cacheSize)

    fun getApiService(): ApiService? {

        if (apiService == null) {
            //val accessToken = Constants.TOKEN
            val okHttpClientBuilder = getUnsafeOkHttpClient()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)


            okHttpClientBuilder.addInterceptor(object : Interceptor {
                override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
                    val request = chain.request()
                    val newRequest = request.newBuilder() //.header("Authorization", accessToken)
                    return chain.proceed(newRequest.build())
                }
            })

            val retrofit = Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .client(okHttpClientBuilder.build())
                .addConverterFactory(GsonConverterFactory.create(buildGson()))
                .build()

            apiService = retrofit.create(ApiService::class.java)
        }

        return apiService
    }

    fun getApiService(cache: Cache, context: Context): ApiService? {


            //val accessToken = Constants.TOKEN
            val okHttpClientBuilder = getUnsafeOkHttpClient().cache(cache)
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .readTimeout(60, TimeUnit.SECONDS)
                    .writeTimeout(60, TimeUnit.SECONDS)

        try {
            okHttpClientBuilder.addInterceptor {
                    chain ->
                var request = chain.request()
                request = if (hasNetwork(context)!!)
                    request.newBuilder().header("Cache-Control", "public, max-age=" + 5).build()
                else
                    request.newBuilder().header("Cache-Control", "public, only-if-cached, max-stale=" + 60 * 60 * 24 * 7).build()

                        chain.proceed(request)

                }

        }catch (e:Exception)
        {
            e.message?.let { Log.d("Exception", it) }
        }

            val retrofit = Retrofit.Builder()
                    .baseUrl(Constants.BASE_URL)
                    .client(okHttpClientBuilder.build())
                    .addConverterFactory(GsonConverterFactory.create(buildGson()))
                    .build()

            apiService = retrofit.create(ApiService::class.java)


        return apiService
    }
    fun hasNetwork(context: Context): Boolean? {
        var isConnected: Boolean? = false // Initial Value
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
        if (activeNetwork != null && activeNetwork.isConnected)
            isConnected = true
        return isConnected
    }
    fun getApiServicePassword(): ApiService? {

        if (apiServiceFor == null) {
            val accessToken = Credentials.basic("SYSTEM", "HANAB1Admin")
            val okHttpClientBuilder = getUnsafeOkHttpClient()
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .readTimeout(60, TimeUnit.SECONDS)
                    .writeTimeout(60, TimeUnit.SECONDS)


            okHttpClientBuilder.addInterceptor(object : Interceptor {
                override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
                    val request = chain.request()
                    val newRequest = request.newBuilder().header("Authorization", accessToken)
                    return chain.proceed(newRequest.build())
                }
            })

            val retrofit = Retrofit.Builder()
                    .baseUrl(Constants.BASE_URL_FORGET)
                    .client(okHttpClientBuilder.build())
                    .addConverterFactory(GsonConverterFactory.create(buildGson()))
                    .build()

            apiServiceFor = retrofit.create(ApiService::class.java)
        }

        return apiServiceFor
    }

    private fun buildGson(): Gson {
        return GsonBuilder()
                .serializeNulls()
                .create()
    }



}