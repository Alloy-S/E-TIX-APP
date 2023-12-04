package com.alloys.e_tix

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.alloys.e_tix.databinding.ActivityRegisterBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.auth
import com.google.firebase.auth.userProfileChangeRequest
import com.google.firebase.firestore.firestore

class Register : AppCompatActivity() {
    private lateinit var binding : ActivityRegisterBinding
    private lateinit var firebaseAuth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val db = Firebase.firestore
        firebaseAuth = FirebaseAuth.getInstance()

        binding.btnRegist.setOnClickListener {
            var name = binding.etName.text.toString()
            val email = binding.etEmail.text.toString()
            val pass = binding.etPass.text.toString()
            val confirmPass = binding.etRePass.text.toString()
            val user = Firebase.auth.currentUser


            if (email.isNotBlank() && pass.isNotEmpty() && confirmPass.isNotEmpty() && name.isNotEmpty()) {
                if (pass == confirmPass) {
                    firebaseAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // User registration is successful
                            val currentUser = firebaseAuth.currentUser

//                            create data in firestore db
                            val data = hashMapOf(
                                "full_name" to name,
                            )
                            db.collection("users").document(currentUser!!.uid).set(data).addOnSuccessListener {
                                Log.d("ADDED TO FIRESTORE", "succes")
                            }

                            // Set additional user information
                            val profileUpdates = UserProfileChangeRequest.Builder()
                                .setDisplayName(name)   // Set display name
                                .setPhotoUri(null)      // You can set a profile photo URI if needed
                                .build()

                            currentUser?.updateProfile(profileUpdates)?.addOnCompleteListener { profileTask ->
                                if (profileTask.isSuccessful) {
                                    // Additional user information set successfully
                                    // You can navigate to the main activity or perform other actions here
                                    val intent = Intent(this, MainActivity::class.java)
                                    startActivity(intent)
                                } else {
                                    Toast.makeText(this, profileTask.exception?.message, Toast.LENGTH_SHORT).show()
                                }
                            }
                        } else {
                            // User registration failed
                            Toast.makeText(this, task.exception?.message, Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    // Passwords do not match
                    Toast.makeText(this, "Password is not matching", Toast.LENGTH_SHORT).show()
                }
            } else {
                // Empty fields not allowed
                Toast.makeText(this, "Empty Fields are not Allowed", Toast.LENGTH_SHORT).show()
            }
        }
    }
}