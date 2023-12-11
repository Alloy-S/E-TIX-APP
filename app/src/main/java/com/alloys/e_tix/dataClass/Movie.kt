package com.alloys.e_tix.dataClass

data class Movie(
    val judul_film: String,
    val deskripsi: String,
    val durasi: String,
    val produser: String,
    val sutradara: String,
    val penulis: String,
    val casts: String,
    val jenis_film: List<String>,
    val urlPoster: String,
    val produksi: String
)
