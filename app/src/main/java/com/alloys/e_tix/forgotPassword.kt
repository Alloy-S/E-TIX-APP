package com.alloys.e_tix

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.alloys.e_tix.databinding.ActivityForgotPasswordBinding
import com.alloys.e_tix.databinding.ActivityMainBinding
import com.alloys.e_tix.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth


class forgotPassword : AppCompatActivity() {
    private lateinit var binding: ActivityForgotPasswordBinding
    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        val currentUser = firebaseAuth.currentUser
        var firebaseAuth = FirebaseAuth.getInstance()
        val _etEmail = findViewById<EditText>(R.id.etForgotPasswordEmail)
        val _btnReset = findViewById<Button>(R.id.btnReset)

        binding.btnReset.setOnClickListener {
            val sPassword = _etEmail.text.toString()
            firebaseAuth.sendPasswordResetEmail(sPassword).addOnSuccessListener {
                Toast.makeText(this,"Please Check Your Email!",Toast.LENGTH_SHORT).show()
            }
                .addOnFailureListener {
                    Toast.makeText(this,it.toString(),Toast.LENGTH_SHORT).show()
                }
        }
        binding.btnBackForgot.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}