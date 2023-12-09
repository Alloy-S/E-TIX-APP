package com.alloys.e_tix

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.alloys.e_tix.helper.DialogHelper
import com.alloys.e_tix.helper.DialogHelper.dismissDialog
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.journeyapps.barcodescanner.BarcodeEncoder
import org.w3c.dom.Text
import java.text.SimpleDateFormat
import kotlin.math.log


class detailTiket : AppCompatActivity() {
    private val db = Firebase.firestore
    private val auth = Firebase.auth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_tiket)

        val _tvTanggalTransaksi = findViewById<TextView>(R.id.tvTanggalTransaksi)
        val _tvJudulFilm = findViewById<TextView>(R.id.tvJudulFilm)
        val _tvNamaMallDetail = findViewById<TextView>(R.id.tvNamaMallDetail)
        val _tvTanggalShow = findViewById<TextView>(R.id.tvTanggalShow)
        val _tvJamShow = findViewById<TextView>(R.id.tvJamShow)
        val _tvBookingCode = findViewById<TextView>(R.id.tvBookingCode)
        val _tvSeats = findViewById<TextView>(R.id.tvSeats)
        val _tvStudio = findViewById<TextView>(R.id.tvStudio)
        val _tvTotalTicket  = findViewById<TextView>(R.id.tvTotalTicket)
        val _tvTiketPrice = findViewById<TextView>(R.id.tvTitalPrice)
        val _tvAdmFee = findViewById<TextView>(R.id.tvAdmFee)
        val _tvTotalOrder = findViewById<TextView>(R.id.tvTotalOrder)
        val _tvPayment = findViewById<TextView>(R.id.tvPayment)
        val _tvTotalPayment = findViewById<TextView>(R.id.tvTotalPayment)
        val imageView = findViewById<ImageView>(R.id.ivBarcode)

        val uidTransaksi = intent.getStringExtra("UIDTransaksi")
        Log.d("UID current user", auth.currentUser!!.uid)
        DialogHelper.showDialogBar(this, "Loading....")
        val isDialogVisible = DialogHelper.isDialogVisible()
        db.collection("users").document(auth.currentUser!!.uid).collection("transaction").document(uidTransaksi.toString()).get().addOnSuccessListener {
            Log.d("UID transaksi", uidTransaksi.toString())
            Log.d("isi detail transaksi", it.data.toString())
            val movieID = it.data?.get("movieId")
            val tangglTransaksi = it.data?.get("transaction_date").toString().toLong()
            val namaMall = it.data?.get("location").toString()
            val showDate = it.data?.get("show_date").toString().toLong()
            val bookingCode = it.data?.get("booking_code").toString()
            val seats = it.data?.get("seats") as List<String>
            val studio = it.data?.get("studio").toString()
            val totalTiket = it.data?.get("total_tiket").toString().toInt()
            val tiketPrice = it.data?.get("harga_tiket").toString().toInt()
            val admFee = it.data?.get("admFee").toString()
            val totalOrder = it.data?.get("total_order").toString().toInt()
            val payment = it.data?.get("payment").toString()
            val totalPayment = it.data?.get("total_order").toString().toInt()

            db.collection("movies").document(movieID.toString()).get().addOnSuccessListener {
                Log.d("get Movie data", it.data.toString())
                val multiFormatWriter = MultiFormatWriter();
                try {
                    val bitMatrix = multiFormatWriter.encode(bookingCode, BarcodeFormat.QR_CODE,400,400);
                    val barcodeEncoder = BarcodeEncoder();
                    val bitmap = barcodeEncoder.createBitmap(bitMatrix);
                    imageView.setImageBitmap(bitmap);
                } catch (e: WriterException) {
                    e.printStackTrace();
                }
                val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
                val dateFormat2 = SimpleDateFormat("dd/MM/yyyy HH:mm")

                val dateshow = dateFormat2.format(showDate).split(" ")

                dismissDialog()
                _tvTanggalTransaksi.setText(dateFormat.format(tangglTransaksi))
                _tvJudulFilm.setText(it.data?.get("judul_film").toString())
                _tvNamaMallDetail.setText(namaMall)
                _tvTanggalShow.setText(dateshow[0])
                _tvJamShow.setText(dateshow[1])
                _tvSeats.setText(seats.toString())
                _tvStudio.setText(studio)
                _tvTotalTicket.setText(totalTiket.toString())
                _tvTiketPrice.setText(tiketPrice.toString())
                _tvTotalOrder.setText(totalOrder.toString())
                _tvPayment.setText(payment)
                _tvTotalPayment.setText(totalPayment.toString())
                _tvBookingCode.setText(bookingCode)
                _tvAdmFee.setText(admFee)
            }
        }



    }
}