package com.bistun.homeassistant.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.databinding.DataBindingUtil
import com.bistun.homeassistant.R
import com.bistun.homeassistant.`interface`.DeviceService
import com.bistun.homeassistant.databinding.ActivityCommandBinding
import com.bistun.homeassistant.model.DeviceCommandModel
import com.bistun.homeassistant.util.RetrofitInstance
import com.bistun.homeassistant.viewModel.DeviceCommandViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class CommandActivity : AppCompatActivity(),View.OnClickListener {
    private var deviceId:Int=0
    private var commandList:List<DeviceCommandModel> = emptyList()
    private lateinit var toggleBtn:Button
    private var countId:Int=10
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_command)
        val activity:ActivityCommandBinding = DataBindingUtil.setContentView(this,R.layout.activity_command)
        activity.viewModel = DeviceCommandViewModel()
        activity.executePendingBindings()

        deviceId =Integer.parseInt(intent.getStringExtra("device_id").toString())
        Log.e("rec:",intent.getStringExtra("device_id").toString())
        toggleBtn =findViewById(R.id.toggle_btn)
        toggleBtn.setOnClickListener(this)
        getDeviceInfo(deviceId)
    }

    private fun getDeviceInfo(deviceId: Int) {
        val retrofit: Retrofit? = RetrofitInstance.getRetrofitInstance()
        val server: DeviceService? = retrofit?.create(DeviceService::class.java)
        server?.getDeviceInfo(deviceId)?.enqueue(object : Callback<List<DeviceCommandModel>> {
            override fun onResponse(
                call: Call<List<DeviceCommandModel>>,
                response: Response<List<DeviceCommandModel>>
            ) {
                if (response.body()?.size!!>0){
                    commandList = response.body()!!
                }

            }

            override fun onFailure(call: Call<List<DeviceCommandModel>>, t: Throwable) {
                Log.e("onFailure:",t.message!!)
            }


        })
    }

    override fun onClick(p0: View?) {
        when(p0){
            toggleBtn->sendCommandToggle()
        }
    }

    private fun sendCommandToggle() {
        val msg ="{\"type\":\"call_service\",\"domain\":\"homeassistant\",\"service\":\"toggle\",\"service_data\":{\"entity_id\":\"light.172_16_27_110\"},\"id\":${countId++}}"
        Log.e("send:",msg)
        MainActivity.ws.sendText(msg)
    }
}