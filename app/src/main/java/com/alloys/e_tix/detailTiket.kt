package com.alloys.e_tix

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.journeyapps.barcodescanner.BarcodeEncoder
import org.w3c.dom.Text


class detailTiket : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_tiket)

        val _tvTanggalTransaksi = findViewById<TextView>(R.id.tvTanggalTransaksi)
        val _tvJudulFilm = findViewById<TextView>(R.id.tvJudulFilm)
        val _tvNamaMallDetail = findViewById<TextView>(R.id.tvNamaMallDetail)
        val _tvTanggalShow = findViewById<TextView>(R.id.tvTanggalShow)
        val _tvBookingCode = findViewById<TextView>(R.id.tvBookingCode)
        val _tvSeats = findViewById<TextView>(R.id.tvSeats)
        val _tvStudio = findViewById<TextView>(R.id.tvStudio)
        val _tvTotalTicket  = findViewById<TextView>(R.id.tvTotalTicket)
        val _tvTitalPrice = findViewById<TextView>(R.id.tvTitalPrice)
        val _tvAdmFee = findViewById<TextView>(R.id.tvAdmFee)
        val _tvTotalOrder = findViewById<TextView>(R.id.tvTotalOrder)
        val _tvPayment = findViewById<TextView>(R.id.tvPayment)
        val _tvTotalPayment = findViewById<TextView>(R.id.tvTotalPayment)


        val imageView = findViewById<ImageView>(R.id.ivBarcode)

        val text = "09878" // Whatever you need to encode in the QR code
        val multiFormatWriter = MultiFormatWriter();
        try {
            val bitMatrix = multiFormatWriter.encode(text, BarcodeFormat.QR_CODE,400,400);
            val barcodeEncoder = BarcodeEncoder();
            val bitmap = barcodeEncoder.createBitmap(bitMatrix);
            imageView.setImageBitmap(bitmap);
        } catch (e: WriterException) {
            e.printStackTrace();
        }

    }
}