package com.bistun.homeassistant.model

import com.google.gson.annotations.SerializedName

class DeviceModel {
    @SerializedName("id")
    val id : Int?=null

    @SerializedName("type_id")
    val typeId : Int?=null

    @SerializedName("ip")
    val ip : String?=null

    @SerializedName("name")
    val name : String?=null

    @SerializedName("user_id")
    val userId : Int?=null

    @SerializedName("end_point")
    val endPoint : String?=null

    @SerializedName("zone_id")
    val zoneId : Int?=null
}