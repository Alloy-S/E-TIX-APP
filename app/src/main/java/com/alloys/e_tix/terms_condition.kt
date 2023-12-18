package com.alloys.e_tix

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.alloys.e_tix.movieFragment
import com.alloys.e_tix.profileFragment
import com.alloys.e_tix.upcomingFragment

class terms_condition : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_terms_condition)
        val _backBtn = findViewById<ImageView>(R.id.backButton)
        _backBtn.setOnClickListener {
            onBackPressed()
        }
    }
}