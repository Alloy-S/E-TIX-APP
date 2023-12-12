package com.alloys.e_tix

import android.app.Activity
import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.alloys.e_tix.dataClass.Movie
import com.google.firebase.Firebase
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.squareup.picasso.Picasso

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [fragmentAddMovieAdmin.newInstance] factory method to
 * create an instance of this fragment.
 */
class fragmentAddMovieAdmin : Fragment(), AdapterView.OnItemSelectedListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private val PICK_IMAGE_REQUEST: Int = 1
    private lateinit var mButtonChooseImage: Button
    private lateinit var mEditTextFileName: EditText
    private lateinit var mImageView: ImageView
    private lateinit var mImageUri: Uri
    private lateinit var mStorageRef : StorageReference
    private lateinit var mDatabaseRef : DatabaseReference
    val db = Firebase.firestore
    var idMovie : String = ""
    var selectedItem: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var spinner: Spinner = view.findViewById(R.id.spStatus)
        var adapter: ArrayAdapter<CharSequence> = ArrayAdapter.createFromResource(requireContext(),R.array.status,
            com.google.android.material.R.layout.support_simple_spinner_dropdown_item)

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinner.adapter = adapter
        spinner.onItemSelectedListener = this

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


        //Buat image
        mButtonChooseImage = view.findViewById(R.id.btChooseImage)
        mImageView = view.findViewById(R.id.ivImage)
        mEditTextFileName = view.findViewById(R.id.etNamaImage)
        mStorageRef = FirebaseStorage.getInstance().getReference("img_poster_film")
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("img_poster_film")

        //Buat buka file di hp
        mButtonChooseImage.setOnClickListener {
            openFileChooser()
        }

        _btnNext.setOnClickListener {
            //Buat tambah data
            TambahData(_etJudul.text.toString(),
                _etDeskripsi.text.toString(),
                _etDurasi.text.toString().toInt(),
                _etProduser.text.toString(),
                _etSutradara.text.toString(),
                _etPenulis.text.toString(),
                _etCasts.text.toString(),
                selectedGenres,
                "${mEditTextFileName.text}.${getFileExtension(mImageUri)}",
                _etProduksi.text.toString(),
                generateRandomStringId())

            //Buat upload image
            uploadFile()
        }
    }

    fun openFileChooser() {
        val intent = Intent()
        intent.setType("image/*")
        intent.setAction(Intent.ACTION_GET_CONTENT)
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    fun getFileExtension(uri : Uri) : String? {
        val cR: ContentResolver = requireContext().contentResolver
        val mime: MimeTypeMap = MimeTypeMap.getSingleton()
        return mime.getExtensionFromMimeType(cR.getType(uri))
    }
    fun uploadFile() {
        val fileReference: StorageReference = mStorageRef.child("${mEditTextFileName.text}.${getFileExtension(mImageUri)}")
        fileReference.putFile(mImageUri)
            .addOnSuccessListener {taskSnapshot: UploadTask.TaskSnapshot ->
                Toast.makeText(requireContext(), "Upload Successful", Toast.LENGTH_SHORT).show()
                val upload = Upload(mEditTextFileName.text.toString().trim(), taskSnapshot.toString())
                val uploadId : String? = mDatabaseRef.push().key
                if (uploadId != null) {
                    mDatabaseRef.child(uploadId).setValue(upload)
                }
            }.addOnFailureListener {
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK
            && data != null && data.data != null){
            mImageUri = data.data!!

            Picasso.get().load(mImageUri).into(mImageView)
            mImageView.setImageURI(mImageUri)
        }
    }

    fun generateRandomStringId(): String {
        val firestore = FirebaseFirestore.getInstance()
        // Create a reference to a new document with an auto-generated ID
        val documentReference = firestore.collection("movies").document()
        return documentReference.id
    }

    fun TambahData(judul_film : String, deskripsi : String, durasi : Int, produser : String, sutradara : String,
                   penulis : String, casts : String, jenis_film : List<String>, urlPoster : String, produksi : String, id: String){
        val dataBaru = Movie(
            id,
            judul_film,
            deskripsi,
            durasi.toString(),
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

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        selectedItem = parent?.getItemAtPosition(position).toString()
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Not yet implemented")
    }
}