package com.alloys.e_tix

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Firebase
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.model.Document
import com.google.firebase.storage.storage


class selectStudio : AppCompatActivity() {

    lateinit var _rvMallOption: RecyclerView

    private val db = Firebase.firestore
    private var storage = Firebase.storage("gs://e-tix-8c2b4.appspot.com")
//    val storageRef = storage.reference
//    val pathReference = storageRef.child("image/poster.jpg")
//    val getReference =
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_studio)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        val _btnBack = findViewById<ImageView>(R.id.btnBack)
        val _tvJudulFilm = findViewById<TextView>(R.id.tvJudulFilmStudio)
        val _tvDurasi = findViewById<TextView>(R.id.tvDurasi)
        val _ivPoster = findViewById<ImageView>(R.id.ivPosterStudio)

        var arMall = ArrayList<Mall>()


        db.collection("movies").document("BKryf5atmLbczKBae3l0").get().addOnSuccessListener {
            val readData = Movie(
                it.data?.get("judul_film").toString(),
                it.data?.get("deskripsi").toString(),
                it.data?.get("durasi").toString().toInt(),
                it.data?.get("produser").toString(),
                it.data?.get("sutradara").toString(),
                it.data?.get("penulis").toString(),
                it.data?.get("casts").toString(),
                it.data?.get("jenis_film") as List<String>,
                it.data?.get("urlPoster").toString(),
                it.data?.get("produksi").toString(),
            )

//            db.collection("purchased_seats").document("ZV6jrzhtAWKa8PLapT2l").get().addOnSuccessListener {
//                val movieid = it.data!!.get("movieID") as DocumentReference
//                movieid.get().addOnSuccessListener {
//                    Log.d("inside movieID judul", it.data?.get("judul_film").toString())
//                }
//            }


            Log.d("read data", readData.toString())

            _tvJudulFilm.setText(readData.judul_film)
            _tvDurasi.setText("${readData.durasi} Minutes")

            val imageRes = this.resources.getIdentifier(readData.urlPoster, "drawable", this.packageName)
            _ivPoster.setImageResource(imageRes)
        }

        val movieID = "BKryf5atmLbczKBae3l0"
         db.collection("movies").document(movieID).collection("show_schedule").get().addOnSuccessListener {
            result ->
            arMall.clear()
            for (document in result) {
                    val arShowtime = ArrayList<jadwalFilm>()
                    val data = document.data.get("showtime") as List<Map<*, *>>
                    for (i in data) {
                        Log.d("isi SHOWTIME", i.toString())
                        arShowtime.add(jadwalFilm(i["waktu"].toString(), (i["seats"] as DocumentReference).path))
                    }
                    var readData = Mall(
                        document.id,
                        document.data.get("nama_mall") as String,
                        arShowtime,
                        document.data.get("harga_tiket").toString().toInt(),
                        movieID
                    )

//                    Log.d("MAP FIRESTORE", document)

                    arMall.add(readData)
                }

             _rvMallOption = findViewById(R.id.rvSelectStudio)

             _rvMallOption.layoutManager = LinearLayoutManager(this)
             val adapterS = AdapterSelectStudio(arMall)
             _rvMallOption.adapter = adapterS
        }




        _btnBack.setOnClickListener {
            onBackPressed()
        }
    }




}