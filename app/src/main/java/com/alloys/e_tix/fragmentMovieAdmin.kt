package com.alloys.e_tix

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.widget.Toast.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.alloys.e_tix.adapterRV.adapterMovieAdmin
import com.alloys.e_tix.adapterRV.movieAdapter
import com.alloys.e_tix.dataClass.Movie
import com.alloys.e_tix.dataClass.dataMovie
import com.alloys.e_tix.helper.DialogHelper
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.storage
import java.io.File

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class fragmentMovieAdmin : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var recyclerView: RecyclerView
    private val db = Firebase.firestore
    private var storage = Firebase.storage("gs://e-tix-8c2b4.appspot.com")
    lateinit var movies: dataMovie
    lateinit var _refreshLayout: SwipeRefreshLayout
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_movie_admin, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _refreshLayout = view.findViewById(R.id.swipeRL)


        DialogHelper.showDialogBar(this.context, "Loading....")
        val isDialogVisible = DialogHelper.isDialogVisible()
        recyclerView = view.findViewById(R.id.rvMovieAdmin)
        recyclerView.layoutManager = GridLayoutManager(this.context, 2)
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

                    val localFile = File.createTempFile("img", ".jpg")
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
                                    Log.d("IMAGE URI", imageUri.toString())

                                    movies = dataMovie(arMovie, imageUri)
                                    recyclerView.adapter = adapterMovieAdmin(movies) { movie ->
                                        navigateToEditMovieAdmin(movie)
                                    }
                                    DialogHelper.dismissDialog()
                                }
                            }
                        }
                    }
                } else {
                    makeText(this.context, "Error fetching movies", LENGTH_SHORT).show()
                }
            }

        _refreshLayout.setOnRefreshListener {
            arMovie.clear()
            Log.d("Refresh page", "jalan")
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
                                        Log.d("IMAGE URI", imageUri.toString())

                                        movies = dataMovie(arMovie, imageUri)
                                        recyclerView.adapter = adapterMovieAdmin(movies) { movie ->
                                            navigateToEditMovieAdmin(movie)
                                        }
                                        _refreshLayout.isRefreshing = false
                                    }
                                }
                            }
                        }
                    } else {
                        makeText(this.context, "Error fetching movies", LENGTH_SHORT).show()
                    }
                }

        }
    }

    private fun navigateToEditMovieAdmin(movie: Movie) {
        val intent = Intent(activity, editMovieAdmin::class.java)
        intent.putExtra("judul_film", movie.judul_film)
        intent.putExtra("deskripsi", movie.deskripsi)
        intent.putExtra("durasi", movie.durasi)
        intent.putExtra("produser", movie.produser)
        intent.putExtra("sutradara", movie.sutradara)
        intent.putExtra("penulis", movie.penulis)
        intent.putExtra("casts", movie.casts)
        intent.putExtra("jenis_film", ArrayList(movie.jenis_film))
        intent.putExtra("urlPoster", movie.urlPoster)
        intent.putExtra("produksi", movie.produksi)
        intent.putExtra("URLTrailer", movie.URLTrailer)
        intent.putExtra("status", movie.status)
        intent.putExtra("editMovie", movie)
        startActivity(intent)
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            fragmentMovieAdmin().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
