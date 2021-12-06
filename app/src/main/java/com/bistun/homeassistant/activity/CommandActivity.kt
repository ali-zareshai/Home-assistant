package com.bistun.homeassistant.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import com.bistun.homeassistant.R
import com.bistun.homeassistant.`interface`.DeviceService
import com.bistun.homeassistant.adapter.DevicesAdapter
import com.bistun.homeassistant.model.DeviceCommand
import com.bistun.homeassistant.model.DeviceModel
import com.bistun.homeassistant.util.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class CommandActivity : AppCompatActivity(),View.OnClickListener {
    private var deviceId:Int=0
    private var commandList:List<DeviceCommand> = emptyList()
    private lateinit var toggleBtn:Button
    private var countId:Int=10
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_command)
        deviceId =Integer.parseInt(intent.getStringExtra("device_id").toString())
        Log.e("rec:",intent.getStringExtra("device_id").toString())
        toggleBtn =findViewById(R.id.toggle_btn)
        toggleBtn.setOnClickListener(this)
        getDeviceInfo(deviceId)
    }

    private fun getDeviceInfo(deviceId: Int) {
        val retrofit: Retrofit? = RetrofitInstance.getRetrofitInstance()
        val server: DeviceService? = retrofit?.create(DeviceService::class.java)
        server?.getDeviceInfo(deviceId)?.enqueue(object : Callback<List<DeviceCommand>> {
            override fun onResponse(
                call: Call<List<DeviceCommand>>,
                response: Response<List<DeviceCommand>>
            ) {
                if (response.body()?.size!!>0){
                    commandList = response.body()!!
                }

            }

            override fun onFailure(call: Call<List<DeviceCommand>>, t: Throwable) {
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