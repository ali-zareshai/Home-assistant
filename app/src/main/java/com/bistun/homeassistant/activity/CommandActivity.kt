package com.bistun.homeassistant.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.bistun.homeassistant.R
import com.bistun.homeassistant.`interface`.DeviceService
import com.bistun.homeassistant.model.DeviceCommandModel
import com.bistun.homeassistant.util.RetrofitInstance
import com.skydoves.colorpickerview.ColorEnvelope
import com.skydoves.colorpickerview.ColorPickerDialog
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener
import com.xw.repo.BubbleSeekBar
import org.json.JSONArray
import org.json.JSONObject

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.lang.Exception


class CommandActivity : AppCompatActivity(),View.OnClickListener {
    private var deviceId:Int=0
    private var commandList:List<DeviceCommandModel> = emptyList()
    private lateinit var toggleBtn:Button
    private lateinit var rangBar: BubbleSeekBar
    private lateinit var colorBtn:Button
    private var rangBrigth:Int=0
    private var redColor:Int = 0
    private var greenColor:Int = 0
    private var blueColor:Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_command)


        deviceId =Integer.parseInt(intent.getStringExtra("device_id").toString())
        toggleBtn =findViewById(R.id.toggle_btn)
        rangBar =findViewById(R.id.rang_btn)
        colorBtn =findViewById(R.id.color_btn)
        findViewById<ImageButton>(R.id.activity_my_ads_back_btn).setOnClickListener(object :View.OnClickListener{
            override fun onClick(p0: View?) {
                finish()
            }

        });
        rangBar.onProgressChangedListener=object:BubbleSeekBar.OnProgressChangedListener{
            override fun onProgressChanged(
                bubbleSeekBar: BubbleSeekBar?,
                progress: Int,
                progressFloat: Float,
                fromUser: Boolean
            ) {
                Log.e("progress1:", progress.toString())
            }

            override fun getProgressOnActionUp(
                bubbleSeekBar: BubbleSeekBar?,
                progress: Int,
                progressFloat: Float
            ) {
                Log.e("progress2:", progress.toString())
            }

            override fun getProgressOnFinally(
                bubbleSeekBar: BubbleSeekBar?,
                progress: Int,
                progressFloat: Float,
                fromUser: Boolean
            ) {
                rangBrigth =progress
                sendColor()
            }

        }
        toggleBtn.setOnClickListener(this)
        colorBtn.setOnClickListener(this)
        getDeviceInfo(deviceId)
    }

    private fun sendColor() {
        for(command in commandList){
            if(command.inputType.equals("color")){
                try {
                    var json =JSONObject(command.commandJson)
                    var service =JSONObject(json.getString("service_data"))
                    service.put("rgb_color",JSONArray(intArrayOf(redColor,greenColor,blueColor)))
                    service.put("brightness",rangBrigth)
                    json.put("service_data",service)
                    json.put("id",MainActivity.countId++)
                    Log.e("sned:",json.toString())
                    MainActivity.ws.sendText(json.toString())

                }catch (e:Exception){
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

    override fun onClick(p0: View?) {
        when(p0){
            toggleBtn -> sendCommandToggle()
            colorBtn -> openColorPicker()
        }
    }

    private fun openColorPicker() {
        ColorPickerDialog.Builder(this)
            .setTitle("ColorPicker Dialog")
            .setPreferenceName("MyColorPickerDialog")
            .setPositiveButton(getString(R.string.confirm),object :ColorEnvelopeListener{
                override fun onColorSelected(envelope: ColorEnvelope?, fromUser: Boolean) {
                    redColor =envelope?.argb?.get(1)!!
                    greenColor=envelope?.argb?.get(2)!!
                    blueColor =envelope?.argb?.get(3)!!
                    sendColor()
                }

            })
            .setNegativeButton(
                getString(R.string.cancel)
            ) { dialogInterface, i -> dialogInterface.dismiss() }
            .attachAlphaSlideBar(true) // the default value is true.
            .attachBrightnessSlideBar(true) // the default value is true.
            .setBottomSpace(12) // set a bottom space between the last slidebar and buttons.
            .show()
    }

    private fun sendCommandToggle() {
        for(command in commandList){
            if(command.inputType.equals("toggle")){
                try {
                    val json =JSONObject(command.commandJson)
                    json.put("id",MainActivity.countId++)
                    Log.e("send:", json.toString())
                    MainActivity.ws.sendText(json.toString())
                }catch (e:Exception){
                    Log.e("ex",e.message?:"")
                }

            }
        }

    }
}


