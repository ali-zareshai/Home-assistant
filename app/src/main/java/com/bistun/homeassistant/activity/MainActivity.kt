package com.bistun.homeassistant.activity

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bistun.homeassistant.R
import com.bistun.homeassistant.`interface`.ClickListener
import com.bistun.homeassistant.`interface`.DeviceService
import com.bistun.homeassistant.adapter.DevicesAdapter
import com.bistun.homeassistant.model.DeviceModel
import com.bistun.homeassistant.util.RecyclerTouchListener
import com.bistun.homeassistant.util.RetrofitInstance
import com.neovisionaries.ws.client.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit


class MainActivity : AppCompatActivity() {
    private lateinit var recycleView: RecyclerView
    private var layoutManager: RecyclerView.LayoutManager?=null
    private var listDevices:List<DeviceModel> = emptyList()

    companion object {
        @JvmStatic lateinit var ws: WebSocket
        @JvmStatic var countId:Int=10
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        recycleView =findViewById(R.id.device_recycleview)
        layoutManager = LinearLayoutManager(applicationContext)
        recycleView.layoutManager =layoutManager
        getPersonnelList()
        GlobalScope.launch {
            connectToscket()
        }
        recycleView.addOnItemTouchListener(
            RecyclerTouchListener(applicationContext,
                recycleView, object : ClickListener {
                    override fun onClick(view: View?, position: Int) {
                        val device: DeviceModel = listDevices[position]
                        Log.e("device:", device.name ?: "--")
                        var intent:Intent?=null
                        if (device.typeId==1){
                            intent = Intent(applicationContext, CommandActivity::class.java)
                        }else if (device.typeId==2){
                            intent = Intent(applicationContext, VideoActivity::class.java)
                        }else if(device.typeId==3){
                            intent = Intent(applicationContext, SonoffSwitchActivity::class.java)
                        }

                        intent?.putExtra("device_id", device.id.toString())
                        intent?.putExtra("endpoint",device.endPoint)
                        Log.e("send:", device.id.toString())
                        startActivity(intent)
                    }

                    override fun onLongClick(view: View?, position: Int) {
                        Log.e("onLongClick:", position.toString())
                    }

                })
        )
    }

    private suspend fun connectToscket() {
        try {
            ws=WebSocketFactory().createSocket("ws://172.16.24.5:8123/api/websocket")
            Log.e("ws", "ws ....")
            ws.connect()
            ws.addListener(object : WebSocketAdapter() {
                override fun onTextMessage(websocket: WebSocket?, text: String?) {
                    Log.e("ws:", text ?: "--")
                    try {
                        val json = JSONObject(text)
                        if (json.getString("type").equals("auth_required")) {
                            ws.sendText("{\"type\":\"auth\",\"access_token\":\"eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiI3OTEyNjU4MTNlNjg0OWU5YWJhNjFkNTg5OGMzNGFlMiIsImlhdCI6MTYzNzQ3MjMxMiwiZXhwIjoxOTUyODMyMzEyfQ.nk8H0gQUoL4_XDjnG5V-ya_Ki7CGB6w1bOurp1JIaho\"}")
                        }
                    } catch (e: Exception) {

                    }
                }

                override fun onConnectError(websocket: WebSocket?, exception: WebSocketException?) {
                    Log.e("ws", exception.toString())
                }
            })
        } catch (e: OpeningHandshakeException) {
           Log.e("err1:", e.message ?: "")
        } catch (e: HostnameUnverifiedException) {
            Log.e("err2:", e.message ?: "")
        } catch (e: WebSocketException) {
            Log.e("err3:", e.message ?: "")
        }

    }

    private fun getPersonnelList() {
        val retrofit: Retrofit? = RetrofitInstance.getRetrofitInstance()
        val server: DeviceService? = retrofit?.create(DeviceService::class.java)
        server?.getDevices()?.enqueue(object : Callback<List<DeviceModel>> {
            override fun onResponse(
                call: Call<List<DeviceModel>>,
                response: Response<List<DeviceModel>>
            ) {
                if (response.body()?.size!! > 0) {
                    recycleView.adapter = DevicesAdapter(applicationContext, response.body()!!)
                    listDevices = response.body()!!

                }

            }

            override fun onFailure(call: Call<List<DeviceModel>>, t: Throwable) {
                Log.e("onFailure:", t.message!!)
            }


        })
    }


}