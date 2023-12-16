package com.alloys.e_tix

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatDelegate
import com.google.firebase.auth.FirebaseAuth

class shortcutPageDev : AppCompatActivity() {
//    hehehehhehe
//    halo
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shortcut_page_dev)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);


        val _historySelected = findViewById<Button>(R.id.historySelected)
        val _beranda = findViewById<Button>(R.id.berandaDev)
        val _select = findViewById<Button>(R.id.selectStudioDev)
        val _detailTiket = findViewById<Button>(R.id.detilTiker)
        val _btnLogout = findViewById<Button>(R.id.btnLogOut)
        _beranda.setOnClickListener {
            val intent = Intent(this@shortcutPageDev, Beranda::class.java).apply {
//                putExtra(Beranda.dataTerima, _etHandphone.text.toString())
//                _etHandphone.text.clear()
//                _etPassword.text.clear()

            }
            startActivity(intent)
        }

        _select.setOnClickListener {
            val intent = Intent(this@shortcutPageDev, selectStudio::class.java).apply {
//                putExtra(Beranda.dataTerima, _etHandphone.text.toString())
//                _etHandphone.text.clear()
//                _etPassword.text.clear()

            }
            startActivity(intent)
        }

    _detailTiket.setText("add jadwal movie")
    _detailTiket.setOnClickListener {
        val intent = Intent(this@shortcutPageDev, addJadwalMovieAdmin::class.java).apply {
//                putExtra(Beranda.dataTerima, _etHandphone.text.toString())
//                _etHandphone.text.clear()
//                _etPassword.text.clear()

        }
        startActivity(intent)
    }


    _historySelected.setOnClickListener {
        val intent = Intent(this@shortcutPageDev, History::class.java).apply {

        }
        startActivity(intent)
    }

    _btnLogout.setText("admin")
    _btnLogout.setOnClickListener {
        val intent = Intent(this, adminPage::class.java)
        startActivity(intent)
    }

    }
}