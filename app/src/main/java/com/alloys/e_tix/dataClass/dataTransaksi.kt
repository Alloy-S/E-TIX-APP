package com.alloys.e_tix.dataClass

import android.graphics.Bitmap

data class dataTransaksi(
    val arTransaksi: ArrayList<dataHistory>,
    val arPoster: Map<String, Bitmap>
)
