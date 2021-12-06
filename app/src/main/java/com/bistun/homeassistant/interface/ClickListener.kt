package com.bistun.homeassistant.`interface`

import android.view.View

interface ClickListener {
    fun onClick(view: View?, position: Int)
    fun onLongClick(view: View?, position: Int)
}