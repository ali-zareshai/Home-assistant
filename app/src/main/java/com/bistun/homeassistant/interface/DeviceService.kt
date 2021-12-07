package com.bistun.homeassistant.`interface`

import com.bistun.homeassistant.model.DeviceCommandModel
import com.bistun.homeassistant.model.DeviceModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface DeviceService {
    @GET("devices")
    fun getDevices():Call<List<DeviceModel>>

    @GET("devices/{id}")
    fun getDeviceInfo(@Path("id") id:Int):Call<List<DeviceCommandModel>>
}