package com.alloys.e_tix.dataClass

import android.graphics.Bitmap
import android.net.Uri
import com.alloys.e_tix.dataClass.Movie


data class dataMovie(
//    val judul_film: String,
//    val durasi: String,
//    val urlPoster:String,
    val arMovie: ArrayList<Movie>,
    val arPoster: Map<String, Uri>

)
