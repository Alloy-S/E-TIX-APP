package com.alloys.e_tix

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.alloys.e_tix.databinding.ActivityForgotPasswordBinding
import com.alloys.e_tix.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth

class forgotPassword : AppCompatActivity() {
    private lateinit var binding: ActivityForgotPasswordBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var firebaseAuth = FirebaseAuth.getInstance()
        val _etEmail = findViewById<EditText>(R.id.etForgotPasswordEmail)
        val _btnReset = findViewById<Button>(R.id.btnReset)
        binding.btnReset.setOnClickListener {
        if (_etEmail.text != null){

        }

        }
    }
}