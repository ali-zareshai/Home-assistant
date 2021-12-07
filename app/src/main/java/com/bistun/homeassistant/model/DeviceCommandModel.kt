package com.bistun.homeassistant.model

import com.google.gson.annotations.SerializedName

class DeviceCommandModel {
    @SerializedName("id")
    var id : Int?=null

    @SerializedName("device_id")
    var deviceId : Int?=null

    @SerializedName("device_name")
    var deviceName : String?=null

    @SerializedName("command_name")
    var commadName : String?=null

    @SerializedName("input_type")
    var inputType : String?=null

    @SerializedName("command_json")
    var commandJson : String?=null

}