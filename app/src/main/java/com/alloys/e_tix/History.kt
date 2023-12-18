package com.alloys.e_tix

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alloys.e_tix.adapterRV.adapterHistory
import com.alloys.e_tix.dataClass.Movie
import com.alloys.e_tix.dataClass.dataHistory
import com.alloys.e_tix.dataClass.dataMovie
import com.alloys.e_tix.dataClass.dataTransaksi
import com.alloys.e_tix.helper.DialogHelper
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.storage
import java.io.File

class History : AppCompatActivity() {

    var listHistory = ArrayList<dataHistory>()
    val db = Firebase.firestore
    val auth = Firebase.auth
    val storage = Firebase.storage("gs://e-tix-8c2b4.appspot.com")
    var arMovie = ArrayList<Movie>()
    val imageBitmap = mutableMapOf<String, Bitmap>()
    val imageUri = mutableMapOf<String, Uri>()
    lateinit var dataTransaksi: dataTransaksi
    lateinit var _rvHistory : RecyclerView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)
        _rvHistory =  findViewById(R.id.selectHistory)
        DialogHelper.showDialogBar(this, "Loading....")
        DialogHelper.isDialogVisible()


        Log.d("USER ID", auth.currentUser!!.uid)
        db.collection("users").document(auth.currentUser!!.uid).collection("transaction").get()
            .addOnSuccessListener { results ->
                listHistory.clear()

                for (document in results) {
                    val movieID = document.data.get("movieId").toString()
                    val tangglTransaksi = document.data.get("transaction_date").toString().toLong()
                    val namaMall = document.data.get("location").toString()
                    val showDate = document.data.get("show_date").toString().toLong()
                    val bookingCode = document.data.get("booking_code").toString()
                    val seats = document.data.get("seats") as List<String>
                    val studio = document.data.get("studio").toString()
                    val totalTiket = document.data.get("total_tiket").toString().toInt()
                    val tiketPrice = document.data.get("harga_tiket").toString().toInt()
                    val admFee = document.data.get("admFee").toString()
                    val totalOrder = document.data.get("total_order").toString().toInt()
                    val payment = document.data.get("payment").toString()
                    val totalPayment = document.data.get("total_order").toString().toInt()


                    db.collection("movies").document(movieID).get()
                        .addOnSuccessListener { movieDocument ->
                            val judulFilm: String = movieDocument.getString("judul_film") ?: ""
                            val imageUrl: String = movieDocument.getString("urlPoster") ?: ""

                            val readData = dataHistory(
                                document.id,
                                tangglTransaksi,
                                showDate,
                                bookingCode,
                                namaMall,
                                judulFilm,
                                imageUrl,
                                seats,
                                studio,
                                totalTiket,
                                tiketPrice,
                                admFee,
                                totalOrder,
                                payment,
                                totalPayment,
                            )

                            listHistory.add(readData)
                        }
                }
                    //    START STORAGE
                    val localFile = File.createTempFile("img", ".jpg")
                    //    GET ALL NAME IN THE FOLDER
                    val arDaftarPoster = ArrayList<String>()
                    storage.getReference("img_poster_film/").listAll().addOnSuccessListener { result ->
                        for (item in result.items) {
//                                    Log.d("ISI STORAGE", item.name)
                            arDaftarPoster.add(item.name)
                        }

                        Log.d("ISI ARBITMAP", arDaftarPoster.toString())

                        var counterDownload = 0
                        for (item in arDaftarPoster) {
                            val isImgRef = storage.reference.child("img_poster_film/$item")
                            isImgRef.downloadUrl.addOnSuccessListener {
//                                val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
                                imageUri[item] = it
                                counterDownload++

                                if (counterDownload == arDaftarPoster.size) {
                                    Log.d("ISI ARBITMAP", imageBitmap.size.toString())
                                    listHistory.sortByDescending { it.transaction_date }
                                    dataTransaksi = dataTransaksi(listHistory, imageUri)
                                    _rvHistory.layoutManager = LinearLayoutManager(this)
                                    val adapters = adapterHistory(dataTransaksi)
                                    _rvHistory.adapter = adapters
                                    DialogHelper.dismissDialog()

                                }
                            }
                        }
                    }



            }
            .addOnFailureListener { exception ->
                            // Handle the failure to retrieve movie details
                            Log.e("Firestore", "Error getting movie details: $exception")
                DialogHelper.dismissDialog()
                onBackPressed()
            }
        val _btnBack = findViewById<ImageView>(R.id.btnBack)
        _btnBack.setOnClickListener {
            onBackPressed()
        }
    }
}

