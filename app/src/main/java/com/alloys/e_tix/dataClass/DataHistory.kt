package com.alloys.e_tix.dataClass

import android.media.Image
import android.os.Parcel
import android.os.Parcelable
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
data class dataHistory(
    val transactionID : String?,
    val transaction_date : Long,
    val show_date : Long,
    val booking_code : String?,
    val location : String?,
    val judulFilm: String?,
    val poster : String?,
    val seats: List<String>?,
    val studio: String?,
    val totalTiket: Int,
    val tiketPrice: Int,
    val admfee: String?,
    val totalOrder: Int,
    val payment: String?,
    val totalPayment: Int,
) : Parcelable
