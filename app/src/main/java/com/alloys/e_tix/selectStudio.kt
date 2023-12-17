package com.alloys.e_tix

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.isInvisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alloys.e_tix.adapterRV.AdapterSelectStudio
import com.alloys.e_tix.dataClass.Mall
import com.alloys.e_tix.dataClass.Movie
import com.alloys.e_tix.dataClass.jadwalFilm
import com.alloys.e_tix.helper.DialogHelper.dismissDialog
import com.alloys.e_tix.helper.DialogHelper.isDialogVisible
import com.alloys.e_tix.helper.DialogHelper.showDialogBar
import com.bumptech.glide.Glide
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.storage
import java.io.File


class selectStudio : AppCompatActivity() {

    lateinit var _rvMallOption: RecyclerView

    private val db = Firebase.firestore
    private var storage = Firebase.storage("gs://e-tix-8c2b4.appspot.com")
    val storageRef = storage.reference
//    val pathReference = storageRef.child("image/poster.jpg")
//    val getReference =
    @SuppressLint("SetTextI18n", "SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_studio)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        val _btnBack = findViewById<ImageView>(R.id.btnBack)
        val _tvJudulFilm = findViewById<TextView>(R.id.tvJudulFilmStudio)
        val _tvDurasi = findViewById<TextView>(R.id.tvDurasi)
        val _ivPoster = findViewById<ImageView>(R.id.ivPosterStudio)
        val _ivIconDurasi = findViewById<ImageView>(R.id.ivIconDurasai)

        var arMall = ArrayList<Mall>()
        _tvJudulFilm.isInvisible = true
        _tvDurasi.isInvisible = true
        _ivPoster.isInvisible = true
        _ivIconDurasi.isInvisible = true

        _btnBack.setOnClickListener {
            onBackPressed()
        }




////    START STORAGE
//        val localFile = File.createTempFile("img", ".jpg")
////    GET ALL NAME IN THE FOLDER
//        val arDaftarPoster = ArrayList<String>()
//        val arPoster = ArrayList<Bitmap>()
//        storage.getReference("img_poster_film/").listAll().addOnSuccessListener { result ->
//            for (item in result.items) {
//                Log.d("ISI STORAGE", item.name)
//                arDaftarPoster.add(item.name)
//            }
//        }
//
//        for (item in arDaftarPoster) {
//            val isImgRef = storage.reference.child(item)
//            isImgRef.getFile(localFile).addOnSuccessListener {
//                val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
//                arPoster.add(bitmap)
//            }
//        }

        val dataMovie = intent.getParcelableExtra("dataMovie", Movie::class.java)
        val posterMovie = intent.getParcelableExtra("posterMovie", Uri::class.java)

        if (dataMovie != null && posterMovie != null) {
            showDialogBar(this, "Loading....")
            val isDialogVisible = isDialogVisible()
//            db.collection("movies").document(dataMovie.movieID).get().addOnSuccessListener {
//                val readData = Movie(
//                    it.id,
//                    it.data?.get("judul_film").toString(),
//                    it.data?.get("deskripsi").toString(),
//                    it.data?.get("durasi").toString(),
//                    it.data?.get("produser").toString(),
//                    it.data?.get("sutradara").toString(),
//                    it.data?.get("penulis").toString(),
//                    it.data?.get("casts").toString(),
//                    it.data?.get("jenis_film") as List<String>,
//                    it.data?.get("urlPoster").toString(),
//                    it.data?.get("produksi").toString(),
//                )
                    _tvJudulFilm.setText(dataMovie.judul_film)
                    _tvDurasi.setText("${dataMovie.durasi} Minutes")
//                    _ivPoster.setImageBitmap(posterMovie)
                    Glide.with(this).load(posterMovie).into(_ivPoster)


                    db.collection("movies").document(dataMovie.movieID).collection("show_schedule").get()
                        .addOnSuccessListener { result ->

                            arMall.clear()
                            for (document in result) {
                                val arShowtime = ArrayList<jadwalFilm>()
                                val data = document.data.get("showtime") as List<Map<*, *>>
                                for (i in data) {
                                    Log.d("isi SHOWTIME", i.toString())
                                    arShowtime.add(
                                        jadwalFilm(
                                            i["waktu"].toString(),
                                            i["seats"].toString()
                                        )
                                    )
                                }

                                val sortedArShowtime = arShowtime.sortedBy { it.showtime }.toCollection(ArrayList())

                                var readData = Mall(
                                    document.id,
                                    document.data.get("nama_mall") as String,
                                    sortedArShowtime,
                                    document.data.get("harga_tiket").toString().toInt(),
                                    dataMovie.movieID,
                                    posterMovie
                                )

//                    Log.d("MAP FIRESTORE", document)

                                arMall.add(readData)
                            }

                            _rvMallOption = findViewById(R.id.rvSelectStudio)

                            _rvMallOption.layoutManager = LinearLayoutManager(this)
                            val adapterS = AdapterSelectStudio(arMall)
                            _rvMallOption.adapter = adapterS


                            dismissDialog()
                            _tvJudulFilm.isInvisible = false
                            _tvDurasi.isInvisible = false
                            _ivPoster.isInvisible = false
                            _ivIconDurasi.isInvisible = false
                        }

//            }
        } else {
            onBackPressed()
        }
    }
}