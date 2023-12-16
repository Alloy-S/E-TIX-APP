package com.alloys.e_tix

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.alloys.e_tix.dataClass.Movie
import com.bumptech.glide.Glide
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class detail_Film : AppCompatActivity() {
    private val db = Firebase.firestore
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_film)

        val _btnBuyTiket = findViewById<Button>(R.id.btnBuyTiket)
        val _btnTrailer = findViewById<Button>(R.id.btnTrailer)
        val datamovie = intent.getParcelableExtra("dataMovie", Movie::class.java)
        val posterMovie = intent.getParcelableExtra("posterMovie", Uri::class.java)

        val _hsPoster = findViewById<ImageView>(R.id.hsPoster)
        val _hsJudul = findViewById<TextView>(R.id.hsJudulFilm)
        val _hsGenre = findViewById<TextView>(R.id.hsGenre)
        val _hsDurasi = findViewById<TextView>(R.id.hsDuration)
        val _hsSynopsis= findViewById<TextView>(R.id.hsSynopsis)
        val _hsProducer = findViewById<TextView>(R.id.hsProducer)
        val _hsDirector = findViewById<TextView>(R.id.hsDirector)
        val _hsWriter = findViewById<TextView>(R.id.hsWriter)
        val _hsCast = findViewById<TextView>(R.id.hsCast)

        val genre = StringBuilder()
        for (item in datamovie!!.jenis_film) {
            genre.append("$item ")
        }

//        _hsPoster.setImageBitmap(posterMovie)
        Glide.with(this).load(posterMovie).into(_hsPoster)
        _hsJudul.setText(datamovie!!.judul_film)
        _hsGenre.setText(genre.toString())
        _hsDurasi.setText(datamovie!!.durasi)
        _hsSynopsis.setText(datamovie!!.deskripsi)
        _hsProducer.setText(datamovie!!.produser)
        _hsDirector.setText(datamovie!!.produksi)
        _hsWriter.setText(datamovie!!.penulis)
        _hsCast.setText(datamovie!!.casts)


        var arMovies = ArrayList<Movie>()


        _btnBuyTiket.setOnClickListener {
            val intent = Intent(this, selectStudio::class.java).apply {
                putExtra("dataMovie", datamovie)
                putExtra("posterMovie", posterMovie)
            }

            startActivity(intent)
        }

        _btnTrailer.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(datamovie.URLTrailer));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setPackage("com.google.android.youtube");
            startActivity(intent)
        }



    }


}