package com.alloys.e_tix

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.hanks.passcodeview.PasscodeView
import com.hanks.passcodeview.PasscodeView.PasscodeViewListener


class PINPage : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pinpage)

        val passcodeView = findViewById<PasscodeView>(R.id.passcodeView)
        val uidTransaksi = intent.getStringExtra("UIDTransaksi")
        passcodeView.setPasscodeLength(5) // to set pincode or passcode
            .setLocalPasscode("12369").listener = object : PasscodeViewListener {
            override fun onFail() {
                // to show message when Password is incorrect
                Toast.makeText(this@PINPage, "Password is wrong!", Toast.LENGTH_SHORT).show()
            }

            override fun onSuccess(number: String) {
                // here is used so that when password
                // is correct user will be
                // directly navigated to next activity

                val tiket = Intent(this@PINPage, detailTiket::class.java).apply {
                    putExtra("UIDTransaksi", uidTransaksi)
                }
                finish()
                startActivity(tiket)
            }
        }

    }




}