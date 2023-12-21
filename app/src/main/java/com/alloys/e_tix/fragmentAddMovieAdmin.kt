package com.alloys.e_tix

import android.app.Activity
import android.content.ContentResolver
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
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
import com.alloys.e_tix.helper.DialogHelper
import com.google.firebase.Firebase
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.squareup.picasso.Picasso
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.util.Calendar

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

        _etJudul = view.findViewById<EditText>(R.id.etJudul)
        _etCasts = view.findViewById<EditText>(R.id.etCasts)
        _etDeskripsi = view.findViewById<EditText>(R.id.etDeskripsi)
        _etDurasi = view.findViewById<EditText>(R.id.etDurasi)
        _etPenulis = view.findViewById<EditText>(R.id.etPenulis)
        _etProduksi = view.findViewById<EditText>(R.id.etProduksi)
        _etProduser = view.findViewById<EditText>(R.id.etProduser)
        _etSutradara = view.findViewById<EditText>(R.id.etSutradara)
        _etURLTrailer = view.findViewById<EditText>(R.id.etUrlTrailer)
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
        selectedGenres = mutableListOf()

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
            if (checkData()) {
                cekJudulFilm { isMatch ->
                    if (isMatch) {
                        Toast.makeText(requireContext(), "Title already exists", Toast.LENGTH_SHORT).show()
                    } else {
                        if (::mImageUri.isInitialized) {
                            DialogHelper.showDialogBar(this.context, "Loading....")
                            val isDialogVisible = DialogHelper.isDialogVisible()

                            uploadFile(
                                _etJudul.text.toString(),
                                _etDeskripsi.text.toString(),
                                _etDurasi.text.toString().toInt(),
                                _etProduser.text.toString(),
                                _etSutradara.text.toString(),
                                _etPenulis.text.toString(),
                                _etCasts.text.toString(),
                                selectedGenres,
                                "${mEditTextFileName.text}.${getFileExtension(mImageUri)}",
                                _etProduksi.text.toString(),
                                generateRandomStringId(),
                                _etURLTrailer.text.toString()
                            )
                        } else {
                            Toast.makeText(requireContext(), "Please select an image first", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            } else {
                Toast.makeText(requireContext(), "Please fill in all required fields", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun cekJudulFilm(callback: (Boolean) -> Unit) {
        db.collection("movies")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val judulFilm = document.getString("judul_film")
                    if (judulFilm != null && judulFilm.toLowerCase() == _etJudul.text.toString().toLowerCase()) {
                        callback(true)
                        return@addOnSuccessListener
                    }
                }
                callback(false)
            }
            .addOnFailureListener { e ->
                Log.w("Firestore", "Error getting documents: $e")
                callback(false)
            }
    }

    fun checkData() : Boolean{
        return _etJudul.text.isNotBlank() &&
                _etDeskripsi.text.isNotBlank() &&
                _etDurasi.text.isNotBlank() &&
                _etProduser.text.isNotBlank() &&
                _etSutradara.text.isNotBlank() &&
                _etPenulis.text.isNotBlank() &&
                _etCasts.text.isNotBlank() &&
                selectedGenres.isNotEmpty()
//                this::mImageUri.isInitialized
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

    fun File.reduceFileImage(): File {
        val file = this
        val bitmap = BitmapFactory.decodeFile(file.path)
        var compressQuality = 100
        var streamLength: Int
        val MAXIMAL_SIZE = 100000
        do {
            val bmpStream = ByteArrayOutputStream()
            bitmap?.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpStream)
            val bmpPicByteArray = bmpStream.toByteArray()
            streamLength = bmpPicByteArray.size
            compressQuality -= 5
        } while (streamLength > MAXIMAL_SIZE)
        bitmap?.compress(Bitmap.CompressFormat.JPEG, compressQuality, FileOutputStream(file))
        return file
    }

    fun uploadFile(judul_film : String, deskripsi : String, durasi : Int, produser : String, sutradara : String,
                   penulis : String, casts : String, jenis_film : List<String>, urlPoster : String, produksi : String, id: String, urlTrailer: String) {
        val calendar = Calendar.getInstance()
        Log.d("PATH FILE", mImageUri.path.toString())

        val filename = "${calendar.timeInMillis}.${getFileExtension(mImageUri)}"
        val childRef2: StorageReference = mStorageRef.child(filename)
        val bmp: Bitmap = MediaStore.Images.Media.getBitmap(requireContext().contentResolver, mImageUri)
        val baos = ByteArrayOutputStream()
        var Quality = 100
        var streamLength: Int
        var MAXIMAL_SIZE = 1000000

        do {
            Log.d("COMPRESS QUALITY", Quality.toString())
            val bmpStream = ByteArrayOutputStream()
            bmp?.compress(Bitmap.CompressFormat.JPEG, Quality, bmpStream)
            val bmpPicByteArray = bmpStream.toByteArray()
            streamLength = bmpPicByteArray.size
            Quality -= 5
            if (Quality <= 5) {
                Quality = 3
                break
            }
        } while (streamLength > MAXIMAL_SIZE)
        bmp.compress(Bitmap.CompressFormat.JPEG, Quality, baos)
        val data: ByteArray = baos.toByteArray()

        // Uploading the image
        val uploadTask2: UploadTask = childRef2.putBytes(data)
        uploadTask2.addOnSuccessListener { taskSnapshot ->
            Log.d("TASKSNAPSHOT", taskSnapshot.toString())
            TambahData(judul_film, deskripsi, durasi, produser, sutradara, penulis, casts, jenis_film, filename, produksi, id, urlTrailer, selectedItem)


        }.addOnFailureListener { e ->
            Toast.makeText(this.context, "Upload Failed -> $e", Toast.LENGTH_LONG).show()
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
                   penulis : String, casts : String, jenis_film : List<String>, urlPoster : String, produksi : String, id: String, urlTrailer: String, status: String){
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
            produksi,
            urlTrailer,
            status
        )

        db.collection("movies").add(dataBaru)
            .addOnSuccessListener { documentReference ->
                // DocumentSnapshot added with ID: documentReference.id
                println("DocumentSnapshot added with ID: ${documentReference.id}")
                idMovie = documentReference.id

                DialogHelper.dismissDialog()
                Toast.makeText(this.context, "Upload successful", Toast.LENGTH_LONG).show()
                val intent = Intent(requireContext(), addJadwalMovieAdmin::class.java)
                intent.putExtra("ID_MOVIE", idMovie)
                val fragmentManager = requireActivity().supportFragmentManager
                val mfMovieAdmin = fragmentMovieAdmin()
                fragmentManager.beginTransaction().apply {
                    replace(R.id.containeradmin, mfMovieAdmin, fragmentMovieAdmin::class.java.simpleName)
                    commit()
                }
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