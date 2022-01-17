package com.bistun.homeassistant.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.CompoundButton
import android.widget.ImageButton
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity
import com.bistun.homeassistant.R
import com.bistun.homeassistant.`interface`.DeviceService
import com.bistun.homeassistant.model.DeviceCommandModel
import com.bistun.homeassistant.util.RetrofitInstance
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.lang.Exception


class SonoffSwitchActivity : AppCompatActivity() {
    lateinit var sonOffSwitch:Switch
    private var deviceId:Int=0
    private var commandList:List<DeviceCommandModel> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sonoff_switch)
        deviceId =Integer.parseInt(intent.getStringExtra("device_id").toString())
        sonOffSwitch=findViewById(R.id.switch_sonoff)
        getDeviceInfo(deviceId)
        findViewById<ImageButton>(R.id.activity_my_ads_back_btn).setOnClickListener(object : View.OnClickListener{
            override fun onClick(p0: View?) {
                finish()
            }

        });
        sonOffSwitch.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked){
                sendCommand("on")
                sonOffSwitch.text=getString(R.string.sonoff_on)
            }else{
                sendCommand("off")
                sonOffSwitch.text=getString(R.string.sonoff_off)
            }
        })
    }

    private fun sendCommand(cmd: String) {
        for(command in commandList){
            if(command.inputType.equals(cmd)){
                try {
                    var json = JSONObject(command.commandJson)

                    json.put("id",MainActivity.countId++)
                    Log.e("sned:",json.toString())
                    MainActivity.ws.sendText(json.toString())

                }catch (e: Exception){
                    Log.e("exception:",e.message?:"")
                }

            }
        }
    }

    private fun getDeviceInfo(deviceId: Int) {
        val retrofit: Retrofit? = RetrofitInstance.getRetrofitInstance()
        val server: DeviceService? = retrofit?.create(DeviceService::class.java)
        server?.getDeviceInfo(deviceId)?.enqueue(object : Callback<List<DeviceCommandModel>> {
            override fun onResponse(
                call: Call<List<DeviceCommandModel>>,
                response: Response<List<DeviceCommandModel>>
            ) {
                if (response.body()?.size!! > 0) {
                    commandList = response.body()!!
                }

            }

            override fun onFailure(call: Call<List<DeviceCommandModel>>, t: Throwable) {
                Log.e("onFailure:",t.message!!)
            }


        })
    }
}