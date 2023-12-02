package com.alloys.e_tix

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.ButtonBarLayout

class MainActivity : AppCompatActivity() {
    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //deklarasi untuk login
        var _etHandphone = findViewById<EditText>(R.id.etHandphone)
        var _etPassword = findViewById<EditText>(R.id.etPassword)

        //Button login --> halaman beranda
        val _btnLogin = findViewById<Button>(R.id.btnLogin)
            _btnLogin.setOnClickListener {
                val intent = Intent(this@MainActivity, shortcutPageDev::class.java).apply {
                    putExtra(Beranda.dataTerima, _etHandphone.text.toString())
                    _etHandphone.text.clear()
                    _etPassword.text.clear()

                }
                startActivity(intent)
            }
        val _btnRegister = findViewById<Button>(R.id.btnRegister)
        _btnRegister.setOnClickListener {
            val intent = Intent(this@MainActivity, Register::class.java)
            startActivity(intent)
        }

        //Button ke admin
        var _imgAdmin = findViewById<ImageView>(R.id.imgAdmin)
        _imgAdmin.setOnClickListener {
            val intent = Intent(this@MainActivity, adminPage::class.java)
            startActivity(intent)
        }
    }
}
