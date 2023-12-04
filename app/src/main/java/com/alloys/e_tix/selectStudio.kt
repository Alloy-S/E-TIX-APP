package com.alloys.e_tix

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.storage


class selectStudio : AppCompatActivity() {

    lateinit var _rvMallOption: RecyclerView

    private val db = Firebase.firestore
    private var storage = Firebase.storage("gs://e-tix-8c2b4.appspot.com")
//    val storageRef = storage.reference
//    val pathReference = storageRef.child("image/poster.jpg")
//    val getReference =
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_studio)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        val _btnBack = findViewById<ImageView>(R.id.btnBack)
        val _tvJudulFilm = findViewById<TextView>(R.id.tvJudulFilmStudio)
        val _tvDurasi = findViewById<TextView>(R.id.tvDurasi)
        val _ivPoster = findViewById<ImageView>(R.id.ivPosterStudio)

        var arMall = ArrayList<Mall>()
        arMall.add(Mall("CIPUTRA WORLD SUARABAYA", arrayListOf("10.30", "11.30","13.30", "15.30", "19.30", "10.30", "11.30","13.30", "15.30", "19.30")))
        arMall.add(Mall("TUNJUNGAN 5", arrayListOf("10.30")))
        arMall.add(Mall("TRANS ICON", arrayListOf("11.30","13.30")))
        arMall.add(Mall("TUNJUNGAN PLAZA", arrayListOf("11.30","13.30")))
        arMall.add(Mall("PAKUWON MALL", arrayListOf("11.30","13.30")))
        arMall.add(Mall("PAKUWON TRADE CENTER", arrayListOf("11.30","13.30")))


        db.collection("movies").document("BKryf5atmLbczKBae3l0").get().addOnSuccessListener {

                    var readData = Movie(
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

                Log.d("read data", readData.toString())

                _tvJudulFilm.setText(readData.judul_film)
                _tvDurasi.setText("${readData.durasi} Minutes")
            val res = resources.getDrawable(getResources().getIdentifier("@drawable/${readData.urlPoster}", null, getPackageName()))
                _ivPoster.setImageDrawable(res);
        }


         db.collection("movies").document("BKryf5atmLbczKBae3l0").collection("show_schedule").get().addOnSuccessListener {
            result ->
            arMall.clear()
            for (document in result) {
                    var readData = Mall(
                        document.data.get("nama_mall") as String,
                        document.data.get("showtime") as List<String>,
                    )

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