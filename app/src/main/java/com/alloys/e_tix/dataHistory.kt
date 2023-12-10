package com.alloys.e_tix

import android.media.Image
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import java.util.Date

data class dataHistory(
    val movieId : String,
    val transaction_date : Long,
    val show_date : Long,
    val booking_code : String,
    val location : String,
    val judulFilm: String,
    val poster : String,
    val seats: List<String>,
    val studio: String,
    val totalTiket: Int,
    val tiketPrice: Int,
    val admfee: String,
    val totalOrder: Int,
    val payment: String,
    val totalPayment: Int
)
