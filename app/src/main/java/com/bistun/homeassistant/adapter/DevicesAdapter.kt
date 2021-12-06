package com.bistun.homeassistant.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bistun.homeassistant.R
import com.bistun.homeassistant.model.DeviceModel
import com.bumptech.glide.Glide

class DevicesAdapter(val context: Context,val deviceList:List<DeviceModel>):
    RecyclerView.Adapter<DevicesAdapter.Holder>() {

        inner class Holder(itemView: View): RecyclerView.ViewHolder(itemView) {
            var name: TextView =itemView.findViewById(R.id.name_device)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_device, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val device=deviceList.get(position)
        holder.name.text =device.name
    }

    override fun getItemCount(): Int {
        if (deviceList!=null)
            return deviceList.size

        return 0
    }
}