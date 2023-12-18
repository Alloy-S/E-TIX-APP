package com.alloys.e_tix

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.alloys.e_tix.adapterRV.movieAdapter
import com.alloys.e_tix.dataClass.Movie
import com.alloys.e_tix.dataClass.dataMovie
import com.alloys.e_tix.helper.DialogHelper
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.storage
import java.io.File

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [movieFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class movieFragment : Fragment(){

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    val auth = Firebase.auth
    val user = Firebase.auth.currentUser!!
    private lateinit var recyclerView: RecyclerView
    private lateinit var movieArrayList: ArrayList<dataMovie>
    lateinit var swipeRLMain :SwipeRefreshLayout
    private val db = Firebase.firestore
    private var storage = Firebase.storage("gs://e-tix-8c2b4.appspot.com")
    lateinit var movies: dataMovie
    var arMovie = ArrayList<Movie>()
    val imageBitmap = mutableMapOf<String, Bitmap>()
    val imageUri = mutableMapOf<String, Uri>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        swipeRLMain = view.findViewById(R.id.swipeRLMain)
        DialogHelper.showDialogBar(this.context, "Loading....")
        val isDialogVisible = DialogHelper.isDialogVisible()
        recyclerView = view.findViewById(R.id.rvMovie)
        recyclerView.layoutManager = GridLayoutManager(this.context,2)
        arMovie.clear()
        imageBitmap.clear()

        db.collection("movies").get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {

                    for (document in task.result) {

                        val readData = Movie(
                            document.id,
                            document.data.get("judul_film").toString(),
                            document.data.get("deskripsi").toString(),
                            document.data.get("durasi").toString(),
                            document.data.get("produser").toString(),
                            document.data.get("sutradara").toString(),
                            document.data.get("penulis").toString(),
                            document.data.get("casts").toString(),
                            document.data.get("jenis_film") as List<String>,
                            document.data.get("urlPoster").toString(),
                            document.data.get("produksi").toString(),
                            document.data.get("URLTrailer").toString(),
                            document.data.get("status").toString()
                        )
                        arMovie.add(readData)

                    }

                    //    START STORAGE
                    val localFile = File.createTempFile("img", ".jpg")
                    //    GET ALL NAME IN THE FOLDER
                    val arDaftarPoster = ArrayList<String>()
                    storage.getReference("img_poster_film/").listAll().addOnSuccessListener { result ->
                        for (item in result.items) {
                            Log.d("ISI STORAGE", item.name)
                            arDaftarPoster.add(item.name)
                        }

                        var counterDownload = 0;
                        for (item in arDaftarPoster) {
                            val isImgRef = storage.reference.child("img_poster_film/$item")
                            isImgRef.downloadUrl.addOnSuccessListener {
                                imageUri[item] = it
                                counterDownload++

                                if (counterDownload == arDaftarPoster.size) {

                                    val filteredMovie = arMovie.filter { it.status.equals("Now Playing") }.toCollection(ArrayList())
                                    movies = dataMovie(filteredMovie, imageUri)
                                    recyclerView.adapter = movieAdapter(movies)
                                    DialogHelper.dismissDialog()
                                }
                            }
                        }
                    }
                } else {
                    Toast.makeText(this.context, "Error fetching movies", Toast.LENGTH_SHORT).show()
                }
            }

        swipeRLMain.setOnRefreshListener {
            arMovie.clear()
            db.collection("movies").get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {

                        for (document in task.result) {

                            val readData = Movie(
                                document.id,
                                document.data.get("judul_film").toString(),
                                document.data.get("deskripsi").toString(),
                                document.data.get("durasi").toString(),
                                document.data.get("produser").toString(),
                                document.data.get("sutradara").toString(),
                                document.data.get("penulis").toString(),
                                document.data.get("casts").toString(),
                                document.data.get("jenis_film") as List<String>,
                                document.data.get("urlPoster").toString(),
                                document.data.get("produksi").toString(),
                                document.data.get("URLTrailer").toString(),
                                document.data.get("status").toString()
                            )
                            arMovie.add(readData)

                        }

                        //    START STORAGE
                        val localFile = File.createTempFile("img", ".jpg")
                        //    GET ALL NAME IN THE FOLDER
                        val arDaftarPoster = ArrayList<String>()
                        storage.getReference("img_poster_film/").listAll().addOnSuccessListener { result ->
                            for (item in result.items) {
                                Log.d("ISI STORAGE", item.name)
                                arDaftarPoster.add(item.name)
                            }

                            var counterDownload = 0;
                            for (item in arDaftarPoster) {
                                val isImgRef = storage.reference.child("img_poster_film/$item")
                                isImgRef.downloadUrl.addOnSuccessListener {
                                    imageUri[item] = it
                                    counterDownload++

                                    if (counterDownload == arDaftarPoster.size) {

                                        val filteredMovie = arMovie.filter { it.status.equals("Now Playing") }.toCollection(ArrayList())
                                        movies = dataMovie(filteredMovie, imageUri)
                                        recyclerView.adapter = movieAdapter(movies)
                                        swipeRLMain.isRefreshing = false
                                    }
                                }
                            }
                        }
                    } else {
                        Toast.makeText(this.context, "Error fetching movies", Toast.LENGTH_SHORT).show()
                    }
                }
        }
        val _namaUser = view.findViewById<TextView>(R.id.tvNamaUser)
        _namaUser.text = "Welcome, " + user.displayName
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_movie, container, false)
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment movieFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            movieFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}