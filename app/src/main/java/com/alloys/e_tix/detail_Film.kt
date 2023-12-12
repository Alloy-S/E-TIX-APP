package com.alloys.e_tix

import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.alloys.e_tix.dataClass.Movie
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class detail_Film : AppCompatActivity() {
    private val db = Firebase.firestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_film)

        val _btnBuyTiket = findViewById<Button>(R.id.btnBuyTiket)
        val datamovie = intent.getParcelableExtra("dataMovie", Movie::class.java)
        val posterMovie = intent.getParcelableExtra("posterMovie", Bitmap::class.java)

        _btnBuyTiket.setOnClickListener {
            val intent = Intent(this, selectStudio::class.java).apply {
                putExtra("dataMovie", datamovie)
                putExtra("posterMovie", posterMovie)
            }

            startActivity(intent)
        }



    }


}