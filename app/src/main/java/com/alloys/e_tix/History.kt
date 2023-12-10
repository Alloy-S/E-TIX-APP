package com.alloys.e_tix

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.SimpleAdapter
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.Transaction
import com.google.firebase.firestore.auth.User
import com.google.firebase.firestore.firestore

class History : AppCompatActivity() {

    var listHistory = ArrayList<dataHistory>()
    val db = Firebase.firestore
    val auth = Firebase.auth

//    lateinit var _lvAdapterSimple : SimpleAdapter
//    var barcode =  findViewById<Button>(R.id.barcode)
//    var detail =  findViewById<Button>(R.id.detail)
    lateinit var _rvHistory : RecyclerView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)
        _rvHistory =  findViewById(R.id.selectHistory)


//        db.collection("movies").get()
//            .addOnCompleteListener { task ->
//                if (task.isSuccessful) {
//                    for (document in task.result) {
//                        val judulFilm: String = document.getString("judul_film") ?: ""
//                        val durasi: String = document.getString("durasi") ?: ""
//                        val imageUrl: String = document.getString("urlPoster") ?: ""
//
//                        val movies = dataMovie(judulFilm, durasi,imageUrl)
//                        movieArrayList.add(movies)
//                    }
//                    recyclerView.adapter = movieAdapter(movieArrayList)
//                } else {
//                    Toast.makeText(this.context, "Error fetching movies", Toast.LENGTH_SHORT).show()
//                }
//            }


        db.collection("users").document(auth.currentUser!!.uid).collection("transaction").get()
            .addOnSuccessListener { results ->
                listHistory.clear()

                for (document in results) {
//                    val transactionDate: String = document.getString("transaction_date") ?: ""
//                    val transactionTime: String = document.getString("transaction_time") ?: ""
//                    val bookingCode: String = document.getString("booking_code") ?: ""
//                    val mallName: String = document.getString("mall_name") ?: ""
//                    val movieId: String = document.getString("movie_id") ?: ""

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

                    // Now, let's retrieve movie details using the movie ID
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
                                totalPayment
                            )

                            listHistory.add(readData)

                            // Update RecyclerView adapter here if needed
                            _rvHistory.layoutManager = LinearLayoutManager(this)
                            val adapters = adapterHistory(listHistory)
                            _rvHistory.adapter = adapters
                        }
                        .addOnFailureListener { exception ->
                            // Handle the failure to retrieve movie details
                            Log.e("Firestore", "Error getting movie details: $exception")
                        }
                }
            }
            .addOnFailureListener { exception ->
                // Handle the failure to retrieve user transactions
                Log.e("Firestore", "Error getting user transactions: $exception")
            }



//        db.collection("users").document("IiGl00Z8zFPt732DLEUAVzjrGdJ3").collection("transaction").get().addOnSuccessListener {
//            results-> listHistory.clear()


//            for (document in results) {
//                var readData = dataHistory(
//                    document.id,
//                    document.data.get("transaction_date") as String,
//                    document.data.get("location") as String,
//                    document.data.get("booking_code") as String,
//                    document.data.get("show_date") as String,
//                    document.data.get("urlPoster") as String
//                )
//                listHistory.add(readData)
//            }
//
//
//            var adapters = adapterHistory(listHistory)
//            _rvHistory.adapter = adapters

//        }


    }





//        db.collection("purchased_seats").document("ZV6jrzhtAWKa8PLapT2l").get().addOnSuccessListener {
//            val movieid = it.data!!.get("movieID") as DocumentReference
//            movieid.get().addOnSuccessListener {
//                Log.d("inside movieID judul", it.data?.get("judul_film").toString() )
//            }
//        }

    }
