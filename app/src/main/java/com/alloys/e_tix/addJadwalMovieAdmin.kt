package com.alloys.e_tix

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class addJadwalMovieAdmin : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    val db = Firebase.firestore
    var idMovie: String? = null
    var selectedItem: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_jadwal_movie_admin)
        idMovie = intent.getStringExtra("ID_MOVIE")
        var spinner: Spinner = findViewById(R.id.spLokasi)
        var adapter: ArrayAdapter<CharSequence> = ArrayAdapter.createFromResource(this,R.array.lokasi,
            com.google.android.material.R.layout.support_simple_spinner_dropdown_item)

//        db.collection("theaters").document("hOmYMmqdr16ciXkb3WYZ").get().addOnSuccessListener {
//            val listTheaters = it.data!!.get("theaters_name") as List<String>
//
//            val adapter2 = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, listTheaters)
//
//            spinner.adapter = adapter2
//            spinner.onItemSelectedListener = this
//        }
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinner.adapter = adapter
        spinner.onItemSelectedListener = this

        var _btnBack = findViewById<ImageView>(R.id.btnBack)
        _btnBack.setOnClickListener {
            onBackPressed()
        }

        val jadwalCheckBoxes = listOf<CheckBox>(
            findViewById(R.id.cb1),
            findViewById(R.id.cb2),
            findViewById(R.id.cb3),
            findViewById(R.id.cb4),
            findViewById(R.id.cb5),
            findViewById(R.id.cb6),
            findViewById(R.id.cb7),
            findViewById(R.id.cb8)
        )

        //list buat simpan genre
        val selectedJadwal = mutableListOf<String>()

        //untuk cek checkbox diklik atau tidak
        for (checkBox in jadwalCheckBoxes) {
            checkBox.setOnCheckedChangeListener { _, isChecked ->
                val genre = checkBox.text.toString()
                if (isChecked) {
                    selectedJadwal.add(genre)
                } else {
                    selectedJadwal.remove(genre)
                }
            }
        }

        val _etHarga = findViewById<EditText>(R.id.etHarga)
        val _etMulai = findViewById<EditText>(R.id.etMulai)
        val _etBerakhir = findViewById<EditText>(R.id.etBerakhir)

        val _btnAdd = findViewById<Button>(R.id.btnAdd)
        _btnAdd.setOnClickListener {
            Log.d("SELECTED ITEM", selectedItem)
            Log.d("selected Jadwal", selectedJadwal.toString())
//            TambahJadwal(selectedItem.toString(),selectedJadwal)
            TambahJadwal(selectedItem.toString(),selectedJadwal,_etHarga.text.toString().toInt(),_etMulai.text.toString(),_etBerakhir.text.toString())
        }
    }

    fun TambahJadwal(lokasi : String, jadwal : List<String>, harga : Int, tgl_mulai : String, tgl_berakhir : String) {
//        val dataBaru = JadwalMovie(lokasi, jadwal)
        val dataBaru = hashMapOf(
            "lokasi" to lokasi,
            "jadwal" to jadwal,
            "harga" to harga,
            "tgl_mulai" to tgl_mulai,
            "tgl_berakhir" to tgl_berakhir
        )
        db.collection("movies").document(idMovie.toString()).collection("show_schedule").add(dataBaru)
            .addOnSuccessListener { documentReference ->
                // DocumentSnapshot added with ID: documentReference.id
                println("Jadwal DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                // Handle errors here
                println("Error adding jadwal document: $e")
            }
    }
    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        selectedItem = parent?.getItemAtPosition(position).toString()
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Not yet implemented")
    }
}