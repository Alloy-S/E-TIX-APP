package com.alloys.e_tix

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.alloys.e_tix.databinding.ActivityMainBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import kotlin.math.log

class MainActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainBinding
    private lateinit var  firebaseAuth: FirebaseAuth
    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        binding.btnRegister.setOnClickListener {
            val intent = Intent(this@MainActivity, Register::class.java)
            startActivity(intent)
        }
        binding.btnLogin.setOnClickListener {
            val email = binding.etEmailLogin.text.toString()
            val pass = binding.etPassword.text.toString()

            val currentUser = firebaseAuth.currentUser
            val displayName = currentUser?.displayName
            Log.d("NAMAKUH",displayName.toString())

            if (email.isNotBlank() && pass.isNotEmpty()){

                    firebaseAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener {
                        if(it.isSuccessful){
                            val intent = Intent(this,Beranda::class.java )
                            startActivity(intent)

                        }else{
                            Toast.makeText(this,it.exception.toString(), Toast.LENGTH_SHORT).show()
                        }
//                        val bundle = Bundle()
//                        bundle.putString("Username", displayName.toString())
//                        val fragment = movieFragment()
//                        fragment.arguments = bundle
//                        val fragmentManager = supportFragmentManager
//                        val transaction = fragmentManager.beginTransaction()
//                        transaction.replace(R.id.FragmentMovie, fragment)
////            transaction.addToBackStack(null)  // Optional: Add to back stack if needed
//                        transaction.commit()
                    }
            }else{
                Toast.makeText(this, "Empty Fields are not Allowed", Toast.LENGTH_SHORT).show()
            }

        //Button ke admin
        var _imgAdmin = findViewById<ImageView>(R.id.imgAdmin)
        _imgAdmin.setOnClickListener {
            val intent = Intent(this@MainActivity, adminPage::class.java)
            startActivity(intent)
        }

        //deklarasi untuk login
        var _etHandphone = findViewById<EditText>(R.id.etEmailLogin)
        var _etPassword = findViewById<EditText>(R.id.etPassword)

        //Button login --> halaman beranda
//        val _btnLogin = findViewById<Button>(R.id.btnLogin)
//            _btnLogin.setOnClickListener {
//                val intent = Intent(this@MainActivity, shortcutPageDev::class.java).apply {
//                    putExtra(Beranda.dataTerima, _etHandphone.text.toString())
//                    _etHandphone.text.clear()
//                    _etPassword.text.clear()
//
//                }
//                startActivity(intent)
//            }
//        val _btnRegister = findViewById<Button>(R.id.btnRegister)
//        _btnRegister.setOnClickListener {
//            val intent = Intent(this@MainActivity, Register::class.java)
//            startActivity(intent)
//        }

        }

    override fun onStart() {
        super.onStart()
//        if(firebaseAuth.currentUser != null){
//            val intent = Intent(this@MainActivity, shortcutPageDev::class.java)
//            startActivity(intent)
//        }
    }
    }
}
