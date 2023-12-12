package com.alloys.e_tix.dataClass

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class jadwalFilm(
    val showtime: String,
    val purchased_seat: String?
) : Parcelable
