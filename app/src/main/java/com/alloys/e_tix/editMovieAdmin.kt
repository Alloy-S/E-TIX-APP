package com.alloys.e_tix

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.alloys.e_tix.dataClass.Movie
import com.alloys.e_tix.dataClass.dataMovie
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class editMovieAdmin : AppCompatActivity(), AdapterView.OnItemSelectedListener  {
    private lateinit var _etJudul: EditText
    private lateinit var _etCasts: EditText
    private lateinit var _etDeskripsi: EditText
    private lateinit var _etDurasi: EditText
    private lateinit var _etPenulis: EditText
    private lateinit var _etProduksi: EditText
    private lateinit var _etProduser: EditText
    private lateinit var _etSutradara: EditText
    private lateinit var _etURLTrailer: EditText
    private lateinit var selectedGenres: MutableList<String>
    private lateinit var spinner: Spinner
    var selectedItem: String = ""

    val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_movie_admin)



        _etJudul = findViewById(R.id.etJudul)
        _etCasts = findViewById(R.id.etCasts)
        _etDeskripsi = findViewById(R.id.etDeskripsi)
        _etDurasi = findViewById(R.id.etDurasi)
        _etPenulis = findViewById(R.id.etPenulis)
        _etProduksi = findViewById(R.id.etProduksi)
        _etProduser = findViewById(R.id.etProduser)
        _etSutradara = findViewById(R.id.etSutradara)
        _etURLTrailer = findViewById(R.id.etUrlTrailer)
        spinner = findViewById(R.id.spStatus)

        val _btnsave = findViewById<Button>(R.id.btnSave)
        val _btnEditJadwal = findViewById<Button>(R.id.btneditJadwal)

        var spinner: Spinner = findViewById(R.id.spStatus)
        var adapter: ArrayAdapter<CharSequence> = ArrayAdapter.createFromResource(this,R.array.status,
            com.google.android.material.R.layout.support_simple_spinner_dropdown_item)

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinner.adapter = adapter
        spinner.onItemSelectedListener = this

        val genreCheckBoxes = listOf<CheckBox>(
            findViewById(R.id.cbDrama),
            findViewById(R.id.cbRomance),
            findViewById(R.id.cbComedy),
            findViewById(R.id.cbHorror),
            findViewById(R.id.cbAction),
            findViewById(R.id.cbAdventure),
            findViewById(R.id.cbAnimation),
            findViewById(R.id.cbDocumenter),
            findViewById(R.id.cbMistery),
            findViewById(R.id.cbThriller),
            findViewById(R.id.cbScifi),
            findViewById(R.id.cbFantasy)
        )

        selectedGenres = mutableListOf()

        for (checkBox in genreCheckBoxes) {
            checkBox.setOnCheckedChangeListener { _, isChecked ->
                val genre = checkBox.text.toString()
                if (isChecked) {
                    selectedGenres.add(genre)
                } else {
                    selectedGenres.remove(genre)
                }
            }
        }

        val dataMovie = intent.getParcelableExtra("editMovie", Movie::class.java)
        _etJudul.setText(intent.getStringExtra("judul_film"))
        _etCasts.setText(intent.getStringExtra("casts"))
        _etDeskripsi.setText(intent.getStringExtra("deskripsi"))
        _etDurasi.setText(intent.getStringExtra("durasi"))
        _etPenulis.setText(intent.getStringExtra("penulis"))
        _etProduksi.setText(intent.getStringExtra("produksi"))
        _etProduser.setText(intent.getStringExtra("produser"))
        _etSutradara.setText(intent.getStringExtra("sutradara"))
        _etURLTrailer.setText(intent.getStringExtra("URLTrailer"))
        val jenis_film = intent.getParcelableArrayListExtra("jenis_film", String::class.java)
        val status = intent.getStringExtra("status")

//        selectedGenres = jenis_film!!
        selectedItem = status!!
        when (status) {
             "Now Playing" -> spinner.setSelection(0)
            "UpComing" -> spinner.setSelection(1)
            "Disable" -> spinner.setSelection(2)
        }


        // Log data received from intent
        Log.d("EDIT_CLASS_DATA", "Judul: ${_etJudul.text}")
        Log.d("EDIT_CLASS_DATA", "Casts: ${_etCasts.text}")
        Log.d("EDIT_CLASS_DATA", "Deskripsi: ${_etDeskripsi.text}")
        Log.d("EDIT_CLASS_DATA", "Durasi: ${_etDurasi.text}")
        Log.d("EDIT_CLASS_DATA", "Penulis: ${_etPenulis.text}")
        Log.d("EDIT_CLASS_DATA", "Produksi: ${_etProduksi.text}")
        Log.d("EDIT_CLASS_DATA", "Produser: ${_etProduser.text}")
        Log.d("EDIT_CLASS_DATA", "Sutradara: ${_etSutradara.text}")
        Log.d("EDIT_CLASS_DATA", "URL Trailer: ${_etURLTrailer.text}")

        // Uncomment the following code after checking the values received
        // Log.d("EDIT_CLASS_DATA", "Status: $selectedItem")

        // Uncomment the following code after checking the values received
         for (genre in dataMovie!!.jenis_film) {
             Log.d("EDIT_CLASS_DATA", "Genre: $genre")
             for (item in genreCheckBoxes) {
                 if (genre.equals(item.text)) {
                     item.isChecked = true
                 }
             }
         }

        // Continue the implementation of spinner and other logic here...

        _btnsave.setOnClickListener {
            val newData = Movie(
                dataMovie!!.movieID,
                _etJudul.text.toString(),
                _etDeskripsi.text.toString(),
                _etDurasi.text.toString(),
                _etProduser.text.toString(),
                _etSutradara.text.toString(),
                _etPenulis.text.toString(),
                _etCasts.text.toString(),
                selectedGenres,
                dataMovie.urlPoster,
                _etProduksi.text.toString(),
                _etURLTrailer.text.toString(),
                selectedItem
            )
            db.collection("movies").document(dataMovie.movieID).set(newData).addOnSuccessListener {
                Toast.makeText(this, "Data berhasil diperbarui", Toast.LENGTH_SHORT).show()
            }
        }

        _btnEditJadwal.setOnClickListener {

        }
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        selectedItem = parent?.getItemAtPosition(position).toString()
        // Log selected item
        Log.d("EDIT_CLASS_DATA", "Status: $selectedItem")
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        // Not yet implemented
    }
}
