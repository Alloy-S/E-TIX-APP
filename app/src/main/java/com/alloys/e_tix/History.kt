package com.alloys.e_tix

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.SimpleAdapter
import android.widget.TextView
import com.google.firebase.Firebase
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.firestore

class History : AppCompatActivity() {

    var listHistory = ArrayList<dataHistory>()
    var db = Firebase.firestore

    lateinit var _lvAdapterSimple :SimpleAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        db.collection("movies").document("g5MVjLHzSvGsyfLJu18a").get().addOnSuccessListener {
            val posterURL = it.data!!.get("urlPoster")
        }


        db.collection("users").document(" IiGl00Z8zFPt732DLEUAVzjrGdJ3").get().addOnSuccessListener {
            val transaction = it.data!!.get("transaction") as DocumentReference

            db.collection("transaction").document("3DjlbmU5k3TKkF071HxF").get().addOnSuccessListener {
                val bookingCode = it.data!!.get("booking_code") as DocumentReference
            }
        }



        db.collection("users").document("IiGl00Z8zFPt732DLEUAVzjrGdJ3").collection("transaction").get().addOnSuccessListener {

        }

//        db.collection("purchased_seats").document("ZV6jrzhtAWKa8PLapT2l").get().addOnSuccessListener {
//            val movieid = it.data!!.get("movieID") as DocumentReference
//            movieid.get().addOnSuccessListener {
//                Log.d("inside movieID judul", it.data?.get("judul_film").toString() )
//            }
//        }

    }
}