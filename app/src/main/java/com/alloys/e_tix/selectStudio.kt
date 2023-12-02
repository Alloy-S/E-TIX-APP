package com.alloys.e_tix

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class selectStudio : AppCompatActivity() {

    lateinit var _rvMallOption: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_studio)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        val _btnBack = findViewById<ImageView>(R.id.btnBack)
        var arMall = ArrayList<Mall>()
        arMall.add(Mall("CIPUTRA WORLD SUARABAYA", arrayListOf("10.30", "11.30","13.30", "15.30", "19.30", "10.30", "11.30","13.30", "15.30", "19.30")))
        arMall.add(Mall("TUNJUNGAN 5", arrayListOf("10.30")))
        arMall.add(Mall("TRANS ICON", arrayListOf("11.30","13.30")))
        arMall.add(Mall("TUNJUNGAN PLAZA", arrayListOf("11.30","13.30")))
        arMall.add(Mall("PAKUWON MALL", arrayListOf("11.30","13.30")))
        arMall.add(Mall("PAKUWON TRADE CENTER", arrayListOf("11.30","13.30")))


        _rvMallOption = findViewById(R.id.rvSelectStudio)

        _rvMallOption.layoutManager = LinearLayoutManager(this)
        val adapterS = AdapterSelectStudio(arMall)
        _rvMallOption.adapter = adapterS

        _btnBack.setOnClickListener {
            onBackPressed()
        }
    }




}