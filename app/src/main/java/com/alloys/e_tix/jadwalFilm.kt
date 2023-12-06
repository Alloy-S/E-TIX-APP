package com.alloys.e_tix

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class jadwalFilm(
    val showtime: String,
    val purchased_seat: String?
) : Parcelable
