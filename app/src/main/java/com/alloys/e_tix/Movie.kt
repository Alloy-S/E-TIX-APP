package com.alloys.e_tix

data class Movie(
    val judul_film: String,
    val deskripsi: String,
    val durasi: Int,
    val produser: String,
    val sutradara: String,
    val penulis: String,
    val casts: String,
    val jenis_film: List<String>,
    val urlPoster: String,
    val produksi: String
)
