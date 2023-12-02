package com.alloys.e_tix

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatDelegate

class shortcutPageDev : AppCompatActivity() {
//    hehehehhehe
//    halo
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shortcut_page_dev)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        val _beranda = findViewById<Button>(R.id.berandaDev)
        val _select = findViewById<Button>(R.id.selectStudioDev)
        val _detailTiket = findViewById<Button>(R.id.detilTiker)

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

    _detailTiket.setOnClickListener {
        val intent = Intent(this@shortcutPageDev, detailTiket::class.java).apply {
//                putExtra(Beranda.dataTerima, _etHandphone.text.toString())
//                _etHandphone.text.clear()
//                _etPassword.text.clear()

        }
        startActivity(intent)
    }
    }
}