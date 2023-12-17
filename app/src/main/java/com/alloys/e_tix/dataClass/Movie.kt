package com.alloys.e_tix.dataClass

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Movie(
    val movieID: String,
    val judul_film: String,
    val deskripsi: String,
    val durasi: String,
    val produser: String,
    val sutradara: String,
    val penulis: String,
    val casts: String,
    val jenis_film: List<String>,
    val urlPoster: String,
    val produksi: String,
    val URLTrailer: String,
    val status: String
) : Parcelable
