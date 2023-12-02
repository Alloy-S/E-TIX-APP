package com.alloys.e_tix

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.Button
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import java.text.NumberFormat
import java.util.Currency

class selectSeat : AppCompatActivity() {
    var selectedSeat = ArrayList<String>()
    var purchasedSeat = ArrayList<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_seat)

        val _llSeat = findViewById<LinearLayout>(R.id.llSeat)
        val _tvNamaMall = findViewById<TextView>(R.id.tvNamaMallSeat)
        val _tvtanggal = findViewById<TextView>(R.id.tvtanggalPenayangan)
        val _tvTotalTiket = findViewById<TextView>(R.id.tvTotalTiket)
        val _btnConfirm = findViewById<Button>(R.id.btnConfirm)

        val namaMall = intent.getStringExtra("namaMall")
        val tanggal = intent.getStringExtra("tanggal")
        val waktuMulai = intent.getStringExtra("waktuMulai")
        val hargaTiket = intent.getIntExtra("hargaTiket", 0)
        _tvNamaMall.setText(namaMall)
        _tvtanggal.setText("$tanggal | $waktuMulai")

        val totalBaris = 15
        val totalKolom = 17
        val frameLayoutSize = resources.getDimensionPixelSize(R.dimen.frame_size_seat) // Replace with your desired frame size in pixels
        val frameLayoutParams = LinearLayout.LayoutParams(frameLayoutSize, frameLayoutSize)
        frameLayoutParams.rightMargin = resources.getDimensionPixelSize(R.dimen.frame_margin_seat) // Replace with your desired margin in pixels

        var kolomTitle = 'A'


        purchasedSeat.add("A2")
        purchasedSeat.add("A3")
        purchasedSeat.add("D8")
        purchasedSeat.add("D9")
        purchasedSeat.add("D10")
        purchasedSeat.add("D11")
        purchasedSeat.add("D7")
        purchasedSeat.add("C9")
        purchasedSeat.add("C10")
        purchasedSeat.add("C11")
        purchasedSeat.add("C7")


        for (baris in 1..totalBaris) {
            val linearLayout = LinearLayout(this)
            val llLayoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            llLayoutParams.bottomMargin = resources.getDimensionPixelSize(R.dimen.frame_margin_seat)
            linearLayout.layoutParams = llLayoutParams
            linearLayout.orientation = LinearLayout.HORIZONTAL

            var kolom = 1
            for (i in 1..totalKolom) {

                if (i == 5 || i == 13) {
                    val frameLayout = FrameLayout(this)
                    frameLayout.layoutParams = frameLayoutParams


                    val textView = TextView(this)
                    val textLayoutParams = FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.WRAP_CONTENT,
                        FrameLayout.LayoutParams.WRAP_CONTENT
                    )

                    textLayoutParams.gravity = Gravity.CENTER
                    textView.layoutParams = textLayoutParams
                    textView.text = "$kolomTitle" // Use whatever text you need


                    frameLayout.addView(textView)
                    linearLayout.addView(frameLayout)
                } else {
                    val frameLayout = FrameLayout(this)
                    frameLayout.layoutParams = frameLayoutParams
                    val textView = TextView(this)
                    val textLayoutParams = FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.WRAP_CONTENT,
                        FrameLayout.LayoutParams.WRAP_CONTENT
                    )


                    textView.layoutParams = textLayoutParams
                    textView.text = "$kolomTitle$kolom" // Use whatever text you need
                    textView.setTextColor(
                        ContextCompat.getColor(
                            this,
                            R.color.white
                        )
                    ) // Replace with your desired text color

                    if (!purchasedSeat.contains(textView.text.toString())) {
                        frameLayout.background = ContextCompat.getDrawable(
                            this,
                            R.drawable.square_btn
                        ) // Replace with your actual drawable

                        frameLayout.setOnClickListener {
                            if (selectedSeat.contains(textView.text.toString())) {
                                frameLayout.background = ContextCompat.getDrawable(this, R.drawable.square_btn)
                                selectedSeat.remove(textView.text.toString())
                            } else {
                                frameLayout.background = ContextCompat.getDrawable(this, R.drawable.square_btn_selected)
                                selectedSeat.add(textView.text.toString())
                            }
                            val totalHarga = selectedSeat.size * hargaTiket

                            val hargaFormatted = formatToIDRCurrency(totalHarga)
                            _tvTotalTiket.setText("TiKet: ${selectedSeat.size}, Total:  ${hargaFormatted}")
                            Log.d("SELECTED SEAT", selectedSeat.toString())
                        }

                    } else {
                        frameLayout.background = ContextCompat.getDrawable(
                            this,
                            R.drawable.square_btn_purchased
                        )
                    }




                    textLayoutParams.gravity = Gravity.CENTER
                    frameLayout.addView(textView)
                    linearLayout.addView(frameLayout)
                    kolom++
                }
            }
            _llSeat.addView(linearLayout)
            kolomTitle++
        }



    }

    fun formatToIDRCurrency(value: Int): String {
        val format = NumberFormat.getCurrencyInstance()
        format.maximumFractionDigits = 0
        format.currency = Currency.getInstance("IDR")

        return format.format(value)
    }
}