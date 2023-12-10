package com.alloys.e_tix

import android.graphics.Bitmap
import java.io.File

data class dataMovie(
//    val judul_film: String,
//    val durasi: String,
//    val urlPoster:String,
    val arMovie: ArrayList<Movie>,
    val arPoster: Map<String, Bitmap>
)
