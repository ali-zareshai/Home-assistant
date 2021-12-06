package com.bistun.homeassistant.util

import com.bistun.homeassistant.config.Config
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory




class RetrofitInstance{


    companion object{
        private var retrofitObj:Retrofit? =null
        @JvmStatic
        fun getRetrofitInstance(): Retrofit? {
            if (retrofitObj == null) {
                retrofitObj = Retrofit.Builder()
                    .baseUrl(Config.DIR_API_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

            }
            return retrofitObj
        }
    }

}