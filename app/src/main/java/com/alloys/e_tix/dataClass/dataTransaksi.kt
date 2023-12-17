package com.alloys.e_tix.dataClass

import android.graphics.Bitmap
import android.net.Uri

data class dataTransaksi(
    val arTransaksi: ArrayList<dataHistory>,
    val arPoster: Map<String, Uri>
)
