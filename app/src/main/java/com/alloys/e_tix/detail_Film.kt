package com.alloys.e_tix

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.media.Image
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
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
        val _btnBack = findViewById<ImageView>(R.id.btnBack)
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
        if (datamovie != null) {
            for (item in datamovie.jenis_film) {
                genre.append("$item ")
            }
        }


//        _hsPoster.setImageBitmap(posterMovie)
        if (datamovie != null) {
            Glide.with(this).load(posterMovie).into(_hsPoster)
            _hsJudul.text = datamovie.judul_film
            _hsGenre.text = genre.toString()
            _hsDurasi.text = "${datamovie.durasi} Minutes"
            _hsSynopsis.text = datamovie.deskripsi
            _hsProducer.text = datamovie.produser
            _hsDirector.text = datamovie.produksi
            _hsWriter.text = datamovie.penulis
            _hsCast.text = datamovie.casts
        } else {
            // Handle the case when datamovie is null, show an error message, or take appropriate action.
        }



        var arMovies = ArrayList<Movie>()


        _btnBuyTiket.isVisible = datamovie?.status != "UpComing"
        _btnBuyTiket.setOnClickListener {
            val intent = Intent(this, selectStudio::class.java).apply {
                putExtra("dataMovie", datamovie)
                putExtra("posterMovie", posterMovie)
            }

            startActivity(intent)
        }

        _btnTrailer.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(datamovie?.URLTrailer));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setPackage("com.google.android.youtube");
            startActivity(intent)
        }

        _btnBack.setOnClickListener {
            finish()
        }



    }


}