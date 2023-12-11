package com.alloys.e_tix

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.alloys.e_tix.dataClass.Movie
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class detail_Film : AppCompatActivity() {
    private val db = Firebase.firestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_film)

        var arMovies = ArrayList<Movie>()
        val dataMovie = db.collection("movies").get().addOnSuccessListener {
                result ->
            arMovies.clear()
            for (document in result) {
                val durasi = document.data.get("durasi").toString().toInt()
                val durasiWithMinutes = "$durasi Minutes"

                var readData = Movie(
                    document.data.get("judul_film").toString(),
                    document.data.get("deskripsi").toString(),
                    durasiWithMinutes,
                    document.data.get("produser").toString(),
                    document.data.get("sutradara").toString(),
                    document.data.get("penulis").toString(),
                    document.data.get("casts").toString(),
                    document.data.get("jenis_film") as List<String>,
                    document.data.get("urlPoster").toString(),
                    document.data.get("produksi").toString(),
                )

                arMovies.add(readData)
            }
            Log.d("arMovies", arMovies.toString())

        }

    }


}