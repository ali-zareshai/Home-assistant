package com.bistun.homeassistant.model

import com.google.gson.annotations.SerializedName

class DeviceCommand {
    @SerializedName("id")
    val id : Int?=null

    @SerializedName("device_id")
    val deviceId : Int?=null

    @SerializedName("command_name")
    val commadName : String?=null

    @SerializedName("input_type")
    val inputType : String?=null

    @SerializedName("command_json")
    val commandJson : String?=null

}