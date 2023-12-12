package com.alloys.e_tix.dataClass

data class JadwalMovie(
    val lokasi : String,
    val jadwal : List<String>,
    val harga_tiket : Int,
    val tgl_mulai : String,
    val tgl_berakhir : String
)
