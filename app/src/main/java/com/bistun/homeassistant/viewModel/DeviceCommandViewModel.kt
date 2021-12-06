package com.bistun.homeassistant.viewModel

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.bistun.homeassistant.BR
import com.bistun.homeassistant.model.DeviceCommandModel

class DeviceCommandViewModel: BaseObservable() {
    private lateinit var model:DeviceCommandModel

    init {
        model= DeviceCommandModel()
    }

    @Bindable
    fun getDeviceName():String?{
        return model.deviceName
    }

    fun setDeviceName(name:String?){
        model.deviceName =name
        notifyPropertyChanged(BR.deviceName)

    }


}