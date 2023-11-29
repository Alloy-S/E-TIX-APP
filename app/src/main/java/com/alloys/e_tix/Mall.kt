package com.alloys.e_tix

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Mall(
    var namaMall: String,
    var arWaktu: ArrayList<String>,

) : Parcelable
