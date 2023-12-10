package com.alloys.e_tix

import android.media.Image
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import java.util.Date

data class dataHistory(
    var movieId : String,
    var transaction_date : String,
    var show_date : String,
    var booking_code : String,
    var location : String,
    var judulFilm: String,
    var poster : String

)
