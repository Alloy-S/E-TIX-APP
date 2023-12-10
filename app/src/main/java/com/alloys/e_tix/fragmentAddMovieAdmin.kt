package com.alloys.e_tix

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.alloys.e_tix.dataClass.Movie
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [fragmentAddMovieAdmin.newInstance] factory method to
 * create an instance of this fragment.
 */
class fragmentAddMovieAdmin : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    val db = Firebase.firestore
    var idMovie : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val _etJudul = view.findViewById<EditText>(R.id.etJudul)
        val _etCasts = view.findViewById<EditText>(R.id.etCasts)
        val _etDeskripsi = view.findViewById<EditText>(R.id.etDeskripsi)
        val _etDurasi = view.findViewById<EditText>(R.id.etDurasi)
        val _etPenulis = view.findViewById<EditText>(R.id.etPenulis)
        val _etProduksi = view.findViewById<EditText>(R.id.etProduksi)
        val _etProduser = view.findViewById<EditText>(R.id.etProduser)
        val _etSutradara = view.findViewById<EditText>(R.id.etSutradara)
        val genreCheckBoxes = listOf<CheckBox>(
            view.findViewById(R.id.cbDrama),
            view.findViewById(R.id.cbRomance),
            view.findViewById(R.id.cbComedy),
            view.findViewById(R.id.cbHorror),
            view.findViewById(R.id.cbAction),
            view.findViewById(R.id.cbAdventure),
            view.findViewById(R.id.cbAnimation),
            view.findViewById(R.id.cbDocumenter),
            view.findViewById(R.id.cbMistery),
            view.findViewById(R.id.cbThriller),
            view.findViewById(R.id.cbScifi),
            view.findViewById(R.id.cbFantasy)
        )

        //list buat simpan genre
        val selectedGenres = mutableListOf<String>()

        //untuk cek checkbox diklik atau tidak
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

        var _btnNext = view.findViewById<Button>(R.id.btNext)
        _btnNext.setOnClickListener {
            TambahData(_etJudul.text.toString(),
                _etDeskripsi.text.toString(),
                _etDurasi.text.toString().toInt(),
                _etProduser.text.toString(),
                _etSutradara.text.toString(),
                _etPenulis.text.toString(),
                _etCasts.text.toString(),
                selectedGenres,
                "a",
                _etProduksi.text.toString())

        }
    }

    fun TambahData(judul_film : String, deskripsi : String, durasi : Int, produser : String, sutradara : String,
                   penulis : String, casts : String, jenis_film : List<String>, urlPoster : String, produksi : String){
        val dataBaru = Movie(
            judul_film,
            deskripsi,
            durasi,
            produser,
            sutradara,
            penulis,
            casts,
            jenis_film,
            urlPoster,
            produksi
        )
        db.collection("movies").add(dataBaru)
            .addOnSuccessListener { documentReference ->
                // DocumentSnapshot added with ID: documentReference.id
                println("DocumentSnapshot added with ID: ${documentReference.id}")
                idMovie = documentReference.id
                val intent = Intent(requireContext(), addJadwalMovieAdmin::class.java)
                intent.putExtra("ID_MOVIE", idMovie)
                startActivity(intent)
            }
            .addOnFailureListener { e ->
                // Handle errors here
                println("Error adding document: $e")
            }
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_movie_admin, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment fragmentAddMovieAdmin.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            fragmentAddMovieAdmin().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}