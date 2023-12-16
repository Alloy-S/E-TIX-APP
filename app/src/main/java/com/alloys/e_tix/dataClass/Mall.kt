package com.alloys.e_tix.dataClass

import android.net.Uri
import android.os.Parcel
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Mall(
    val mallID: String,
    val namaMall: String,
    val showtime: ArrayList<jadwalFilm>,
    val hargaTiket: Int,
    val tmpMovieID: String,
    val tmpPoster: Uri
) : Parcelable