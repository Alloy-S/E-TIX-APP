package com.alloys.e_tix

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.storage
import java.io.File
import com.alloys.e_tix.helper.DialogHelper.dismissDialog
import com.alloys.e_tix.helper.DialogHelper.isDialogVisible
import com.alloys.e_tix.helper.DialogHelper.showDialogBar

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [movieFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class movieFragment : Fragment() {

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var recyclerView: RecyclerView
    private lateinit var movieArrayList: ArrayList<dataMovie>
    private lateinit var db : FirebaseFirestore
    private var storage = Firebase.storage("gs://e-tix-8c2b4.appspot.com")
    lateinit var movies: dataMovie

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showDialogBar(this.context, "Loading....")
        val isDialogVisible = isDialogVisible()
        recyclerView = view.findViewById(R.id.rvMovie)
        recyclerView.layoutManager = LinearLayoutManager(this.context)

        movieArrayList = arrayListOf()

        db = FirebaseFirestore.getInstance()

        db.collection("movies").get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val arMovie = ArrayList<Movie>()
                    for (document in task.result) {
//                        val judulFilm: String = document.getString("judul_film") ?: ""
//                        val durasi: String = document.getString("durasi") ?: ""
//                        val imageUrl: String = document.getString("urlPoster") ?: ""

                        val judulFilm: String = document.getString("judul_film") ?: ""
                        val durasi: String = document.getString("durasi") ?: ""
                        val imageFileName: String = document.getString("urlPoster") ?: ""

                        val readData = Movie(
                            document.data.get("judul_film").toString(),
                            document.data.get("deskripsi").toString(),
                            document.data.get("durasi").toString().toInt(),
                            document.data.get("produser").toString(),
                            document.data.get("sutradara").toString(),
                            document.data.get("penulis").toString(),
                            document.data.get("casts").toString(),
                            document.data.get("jenis_film") as List<String>,
                            document.data.get("urlPoster").toString(),
                            document.data.get("produksi").toString(),
                        )

                        arMovie.add(readData)

                    }

                    //    START STORAGE
                    val localFile = File.createTempFile("img", ".jpg")
                    //    GET ALL NAME IN THE FOLDER
                    val arDaftarPoster = ArrayList<String>()
                    val arPoster = ArrayList<Bitmap>()
                    storage.getReference("img_poster_film/").listAll().addOnSuccessListener { result ->
                        for (item in result.items) {
                            Log.d("ISI STORAGE", item.name)
                            arDaftarPoster.add(item.name)
                        }

                        var counterDownload = 0;
                        for (item in arDaftarPoster) {
                            val isImgRef = storage.reference.child("img_poster_film/$item")
                            isImgRef.getFile(localFile).addOnSuccessListener {
                                val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
                                arPoster.add(bitmap)
                                counterDownload++

                                if (counterDownload == arDaftarPoster.size) {
                                    Log.d("ISI POSTER", arPoster.toString())
                                    movies = dataMovie(arMovie, arPoster)
                                    recyclerView.adapter = movieAdapter(movies)
                                    dismissDialog()
                                }
                            }


                        }
                    }




                } else {
                    Toast.makeText(this.context, "Error fetching movies", Toast.LENGTH_SHORT).show()
                }
            }
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

