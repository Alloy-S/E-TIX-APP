package com.alloys.e_tix

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var _etHandphone = findViewById<EditText>(R.id.etHandphone)
        var _etPassword = findViewById<EditText>(R.id.etPassword)

        val _btnLogin = findViewById<Button>(R.id.btnLogin)
            _btnLogin.setOnClickListener {
                val intent = Intent(this@MainActivity, Beranda::class.java).apply {
                    putExtra(Beranda.dataTerima, _etHandphone.text.toString())
                    _etHandphone.text.clear()
                    _etPassword.text.clear()
                }
                startActivity(intent)
            }

        }
    }
