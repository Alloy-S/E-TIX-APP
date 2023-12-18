package com.alloys.e_tix

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import com.alloys.e_tix.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class MainActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainBinding
    private lateinit var  firebaseAuth: FirebaseAuth
    private lateinit var progressBar: ProgressBar
    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        progressBar = findViewById(R.id.progressBar)
        firebaseAuth = FirebaseAuth.getInstance()
        val currentUser = firebaseAuth.currentUser

        var isPasswordVisible = false // Variable to track password visibility

        binding.unvisible.setOnClickListener {
            isPasswordVisible = !isPasswordVisible // Toggle the visibility state

            // Change the visibility of the password EditText
            if (isPasswordVisible) {
                binding.etPassword.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            } else {
                binding.etPassword.inputType =
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            }

            // Move the cursor to the end of the text to maintain the cursor position
            binding.etPassword.setSelection(binding.etPassword.text.length)

            // Change the visibility of the unvisible image
            if (isPasswordVisible) {
                binding.unvisible.setImageResource(R.drawable.visible) // Change to visible icon
            } else {
                binding.unvisible.setImageResource(R.drawable.unvisible) // Change to invisible icon
            }
        }

// Set the initial state of the password to be invisible
        binding.etPassword.inputType =
            InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD

// Change the visibility of the unvisible image to the initial state
        binding.unvisible.setImageResource(R.drawable.unvisible)


        binding.btnRegister.setOnClickListener {
            val intent = Intent(this@MainActivity, Register::class.java)
            startActivity(intent)
        }
        //Button ke admin
        var _imgAdmin = findViewById<ImageView>(R.id.imgAdmin)
        _imgAdmin.setOnClickListener {
            val intent = Intent(this@MainActivity, adminPage::class.java)
            startActivity(intent)
        }
        if (firebaseAuth.currentUser != null) {
            val intent = Intent(this@MainActivity, Beranda::class.java)
            startActivity(intent)
            finish() // Finish the current activity to prevent going back to the login screen
        }
        Log.d("CEK CURRENT USER", firebaseAuth.currentUser?.uid.toString())
        binding.btnLogin.setOnClickListener {
            val email = binding.etEmailLogin.text.toString()
            val pass = binding.etPassword.text.toString()

            if (email.isNotBlank() && pass.isNotEmpty()) {
                showProgressBar()

                firebaseAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener { loginTask ->

                    if (loginTask.isSuccessful) {
                        val currentUser = firebaseAuth.currentUser
                        currentUser?.reload()?.addOnCompleteListener { reloadTask ->
                            hideProgressBar()
                            if (reloadTask.isSuccessful) {
                                checkEmailVerification(currentUser)
                            } else {
                                Toast.makeText(this, "Failed to reload user", Toast.LENGTH_SHORT).show()
                            }
                        }
                    } else {
                        hideProgressBar()
                        if (currentUser?.isEmailVerified == false){
                            checkEmailVerification(currentUser)
                        }else{
                            Toast.makeText(this, "Incorrect email or password", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            } else {
                Toast.makeText(this, "Empty Fields are not Allowed", Toast.LENGTH_SHORT).show()
            }
        }
            binding.tvForgot.setOnClickListener {
                val intent = Intent(this, forgotPassword::class.java)
                startActivity(intent)
            }
        }
    private fun checkEmailVerification(currentUser: FirebaseUser?) {
        if (currentUser?.isEmailVerified == true) {
            val intent = Intent(this, Beranda::class.java)
            startActivity(intent)
        } else {
            Toast.makeText(
                this,
                "Please verify your email first, Check your email",
                Toast.LENGTH_LONG
            ).show()
            currentUser?.sendEmailVerification()?.addOnCompleteListener { verificationTask ->
                if (verificationTask.isSuccessful) {
                    // Email verification sent successfully
                } else {
                    Toast.makeText(
                        this,
                        verificationTask.exception.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
    private fun showProgressBar() {
        progressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        progressBar.visibility = View.INVISIBLE
    }
    override fun onStart() {
        super.onStart()
//        buat matiin login
//        if(firebaseAuth.currentUser != null){
//            val intent = Intent(this@MainActivity, shortcutPageDev::class.java)
//            startActivity(intent)
//        }
    }
}
