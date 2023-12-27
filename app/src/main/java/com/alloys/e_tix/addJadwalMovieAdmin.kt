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
import com.alloys.e_tix.dataClass.showTime
import com.alloys.e_tix.helper.DialogHelper
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class addJadwalMovieAdmin : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    val db = Firebase.firestore
    var idMovie: String? = null
    var selectedItem: String = ""
    lateinit var _etHarga: EditText
    lateinit var jadwalCheckBoxes: List<CheckBox>
    lateinit var selectedJadwal: MutableList<String>
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

        val _btnSave = findViewById<Button>(R.id.btnUpdate)
        _btnSave.setOnClickListener {
            finish()
        }

        jadwalCheckBoxes = listOf<CheckBox>(
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
        selectedJadwal = mutableListOf<String>()

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

        _etHarga = findViewById<EditText>(R.id.etHarga)

        val _btnAdd = findViewById<Button>(R.id.btnAdd)

        _btnAdd.setOnClickListener {
            if (!_etHarga.text.toString().equals("")) {
                DialogHelper.showDialogBar(this, "Loading....")
                val isDialogVisible = DialogHelper.isDialogVisible()
                Log.d("SELECTED ITEM", selectedItem)
                Log.d("selected Jadwal", selectedJadwal.toString())
                val arShowTime = ArrayList<showTime>()

                var counter = 0
                for (jam in selectedJadwal) {

                    val dataShowtime = hashMapOf(
                        "movieID" to idMovie,
                        "nama_mall" to selectedItem,
                        "showtime" to jam
                    )
                    db.collection("purchased_seats").add(dataShowtime).addOnSuccessListener {
                        arShowTime.add(showTime(it.id, jam))
                        counter++
                        for (checkBox in jadwalCheckBoxes) {
                            Log.d("checkbox text & jam", "${checkBox.text} == $jam")
                            if (checkBox.text.toString().equals(jam)) {
                                Log.d("Checkbox disable before", checkBox.isEnabled.toString())
                                checkBox.isEnabled = false

                            }
                        }
                        if (counter == selectedJadwal.size) {

                            TambahJadwal(selectedItem, arShowTime, _etHarga.text.toString().toInt())
                        }
                    }
                }
            } else {
                Toast.makeText(this, "Harga tiket harap di isi", Toast.LENGTH_SHORT).show()
            }

        }
    }

    fun TambahJadwal(lokasi : String, jadwal : ArrayList<showTime>, harga : Int) {
//        val dataBaru = JadwalMovie(lokasi, jadwal)
        val dataBaru = hashMapOf(
            "nama_mall" to lokasi,
            "showtime" to jadwal,
            "harga_tiket" to harga,
        )
        db.collection("movies").document(idMovie.toString()).collection("show_schedule").add(dataBaru)
            .addOnSuccessListener { documentReference ->
                // DocumentSnapshot added with ID: documentReference.id
                Toast.makeText(this, "Berhasil ditambahkan", Toast.LENGTH_SHORT).show()
                DialogHelper.dismissDialog()
            }
            .addOnFailureListener { e ->
                // Handle errors here
                Log.d("Add add jadwal eror", e.printStackTrace().toString())
            }
    }
    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        selectedItem = parent?.getItemAtPosition(position).toString()
        Log.d("Selected Item", selectedItem)
        for (checkBox in jadwalCheckBoxes) {
            checkBox.isChecked = false
            checkBox.isEnabled = true
        }
        db.collection("movies").document(idMovie.toString()).collection("show_schedule").whereEqualTo("nama_mall", selectedItem)
            .get()
            .addOnSuccessListener { result ->

                if (result.size() == 0) {

                } else {
                    val document = result.documents[0]
                    _etHarga.setText(document.get("harga_tiket").toString())

                    val showTimeList: List<Map<String, String>> =
                        document.get("showtime") as? List<Map<String, String>> ?: emptyList()
                    for ((index, checkBox) in jadwalCheckBoxes.withIndex()) {
                        val waktuCheckBox = checkBox.text.toString()
                        val isTimeChecked =
                            showTimeList.any { it["waktu"] == waktuCheckBox }
                        checkBox.isChecked = isTimeChecked
                        selectedJadwal.add(checkBox.text.toString())
                        checkBox.isEnabled = !isTimeChecked
                    }
                    Log.d("isi selected jadwal", selectedJadwal.toString())
                }
            }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Not yet implemented")
    }
}