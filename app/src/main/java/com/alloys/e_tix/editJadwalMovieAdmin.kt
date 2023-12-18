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

class editJadwalMovieAdmin : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    val db = Firebase.firestore
    var idMovie: String? = null
    var selectedItem: String = ""
    private lateinit var _etHarga: EditText
    private val jadwalCheckBoxes = mutableListOf<CheckBox>()
    var ganti: Boolean = true
    var selectedDocumentId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_jadwal_movie_admin)
        idMovie = intent.getStringExtra("ID_MOVIE")
        var spinner: Spinner = findViewById(R.id.spLokasi)
        var adapter: ArrayAdapter<CharSequence> = ArrayAdapter.createFromResource(
            this, R.array.lokasi,
            com.google.android.material.R.layout.support_simple_spinner_dropdown_item
        )

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinner.adapter = adapter
        spinner.onItemSelectedListener = this

        var _btnBack = findViewById<ImageView>(R.id.btnBack)
        _btnBack.setOnClickListener {
            onBackPressed()
        }

        jadwalCheckBoxes.addAll(listOf(
            findViewById(R.id.cb1),
            findViewById(R.id.cb2),
            findViewById(R.id.cb3),
            findViewById(R.id.cb4),
            findViewById(R.id.cb5),
            findViewById(R.id.cb6),
            findViewById(R.id.cb7),
            findViewById(R.id.cb8)
        ))

        _etHarga = findViewById(R.id.etHarga)

        val _btnAdd = findViewById<Button>(R.id.btnAdd)
        val _btnSave = findViewById<Button>(R.id.btnUpdate)
        _btnSave.setOnClickListener {
            val intent = Intent(this, fragmentMovieAdmin::class.java)
            startActivity(intent)
        }
        _btnAdd.setOnClickListener {
            if (ganti == false) {
                DialogHelper.showDialogBar(this, "Loading....")
                val isDialogVisible = DialogHelper.isDialogVisible()
                Log.d("SELECTED ITEM", selectedItem)
                val selectedJadwal = getSelectedJadwal()
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
                        if (counter == selectedJadwal.size) {
                            TambahJadwal(selectedItem, arShowTime, _etHarga.text.toString().toInt())
                        }
                    }
                }
            }
            if (ganti == true) {
                DialogHelper.showDialogBar(this, "Loading....")
                val isDialogVisible = DialogHelper.isDialogVisible()
                Log.d("SELECTED ITEM", selectedItem)
                val selectedJadwal = getSelectedJadwal()
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
                        if (counter == selectedJadwal.size) {
                            // Menggunakan ID dokumen untuk pembaruan data
                            TambahJadwal(selectedItem, arShowTime, _etHarga.text.toString().toInt())
                        }
                    }
                }
            }
        }
    }

    private fun getSelectedJadwal(): List<String> {
        val selectedJadwal = mutableListOf<String>()
        for (checkBox in jadwalCheckBoxes) {
            if (checkBox.isChecked) {
                selectedJadwal.add(checkBox.text.toString())
            }
        }
        return selectedJadwal
    }

    fun TambahJadwal(lokasi: String, jadwal: ArrayList<showTime>, harga: Int) {
        val dataBaru = hashMapOf(
            "nama_mall" to lokasi,
            "showtime" to jadwal,
            "harga_tiket" to harga,
        )

        if (ganti) {
            // Jika sedang dalam mode edit, gunakan selectedDocumentId untuk penyuntingan data
            db.collection("movies")
                .document(idMovie.toString())
                .collection("show_schedule")
                .document(selectedDocumentId ?: "")
                .set(dataBaru)
                .addOnSuccessListener {
                    Toast.makeText(this, "Berhasil diubah", Toast.LENGTH_SHORT).show()
                    DialogHelper.dismissDialog()
                }
                .addOnFailureListener { e ->
                    Log.d("Edit jadwal error", e.printStackTrace().toString())
                }
        } else {
            // Jika bukan mode edit, tambahkan data baru
            db.collection("movies")
                .document(idMovie.toString())
                .collection("show_schedule")
                .add(dataBaru)
                .addOnSuccessListener { documentReference ->
                    Toast.makeText(this, "Berhasil ditambahkan", Toast.LENGTH_SHORT).show()
                    DialogHelper.dismissDialog()
                }
                .addOnFailureListener { e ->
                    Log.d("Tambah jadwal error", e.printStackTrace().toString())
                }
        }
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val selectedMall = parent?.getItemAtPosition(position).toString()
        db.collection("movies").document(idMovie.toString())
            .collection("show_schedule")
            .whereEqualTo("nama_mall", selectedMall)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val result = task.result
                    if (result != null && !result.isEmpty) {
                        ganti = true
                        val document = result.documents[0]
                        val harga: Long = document.getLong("harga_tiket") ?: 0
                        _etHarga.setText(harga.toString())

                        val showTimeList: List<Map<String, String>> =
                            document.get("showtime") as? List<Map<String, String>> ?: emptyList()
                        for ((index, checkBox) in jadwalCheckBoxes.withIndex()) {
                            val waktuCheckBox = checkBox.text.toString()
                            val isTimeChecked =
                                showTimeList.any { it["waktu"] == waktuCheckBox }
                            checkBox.isChecked = isTimeChecked
                        }

                        // Menyimpan ID dokumen yang sesuai ke dalam variabel global
                        selectedDocumentId = document.id
                    } else {
                        ganti = false
                        Log.d("SELECTED_MALL_CHECK", "Tidak ada data untuk $selectedMall")

                        // Jika tidak ada data, kosongkan _etHarga dan checkbox
                        _etHarga.setText("")
                        for (checkBox in jadwalCheckBoxes) {
                            checkBox.isChecked = false
                        }
                    }
                } else {
                    Log.d("SELECTED_MALL_CHECK", "Error getting documents: ", task.exception)
                }
            }

        selectedItem = parent?.getItemAtPosition(position).toString()
    }


    override fun onNothingSelected(parent: AdapterView<*>?) {
        // ...
    }
}
