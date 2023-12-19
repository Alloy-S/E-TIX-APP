package com.alloys.e_tix

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.alloys.e_tix.dataClass.dataHistory
import com.alloys.e_tix.dataClass.dataTransaksi
import com.alloys.e_tix.helper.Currency.formatNumberWithCommas
import com.alloys.e_tix.helper.DialogHelper
import com.alloys.e_tix.helper.DialogHelper.dismissDialog
import com.bumptech.glide.Glide
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.journeyapps.barcodescanner.BarcodeEncoder
import java.text.SimpleDateFormat


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
        val _ivBarcode = findViewById<ImageView>(R.id.ivBarcode)
        val _ivPosterFilm = findViewById<ImageView>(R.id.ivPosterFilm)
        val _btnBack = findViewById<ImageView>(R.id.btnBack)
        val _btnRefund = findViewById<Button>(R.id.btnRefund)

        findViewById<ImageView>(R.id.btnBack).setOnClickListener {
            finish()
        }
        val uidTransaksi = intent.getStringExtra("UIDTransaksi")
        hideAllViews(findViewById(R.id.detailTikerView))
        Log.d("UID current user", auth.currentUser!!.uid)
        DialogHelper.showDialogBar(this, "Loading....")
        val isDialogVisible = DialogHelper.isDialogVisible()

        val poster = intent.getParcelableExtra("poster", Uri::class.java)
        val dataTransaksi = intent.getParcelableExtra("dataTransaksi", dataHistory::class.java)
        val IDTransaksi = intent.getStringExtra("UIDTransaksi")
        Log.d("data transaksi", dataTransaksi.toString())
//        Log.d("DataTransaksiType", dataTransaksi?.javaClass?.simpleName ?: "null")

        val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
        val dateFormat2 = SimpleDateFormat("dd/MM/yyyy HH:mm")

        _btnRefund.setOnClickListener {
            if (IDTransaksi != null) {
                db.collection("users").document(auth.currentUser!!.uid).collection("transaction").document(IDTransaksi).get().addOnSuccessListener {
                    val dataRefund = hashMapOf(
                        "transaction_date" to it.data?.get("transaction_date").toString().toLong(),
                        "movieId" to it.data?.get("movieId").toString(),
                        "location" to it.data?.get("location").toString(),
                        "booking_code" to it.data?.get("booking_code").toString(),
                        "harga_tiket" to it.data?.get("harga_tiket").toString().toInt(),
                        "studio" to 1,
                        "payment" to "Free",
                        "seats" to it.data?.get("seats") as List<String>,
                        "total_tiket" to it.data?.get("total_tiket").toString().toInt(),
                        "total_order" to it.data?.get("total_order").toString().toInt(),
                        "show_date" to it.data?.get("show_date").toString().toLong(),
                        "admFee" to 2500,
                        "seatsTicketRef" to it.data?.get("seatsTicketRef").toString(),
                        "purchased_seats" to it.data?.get("purchased_seats").toString()
                    )

                    db.collection("purchased_seats").document(dataRefund.get("purchased_seats").toString()).collection("time_of_purchase").document(dataRefund.get("seatsTicketRef").toString()).get().addOnSuccessListener {
                        val date = it.data!!.get("date").toString().toLong()
                        var seats = it.data!!.get("seats") as List<String>
                        var listSeat = seats.toCollection(ArrayList())

                        for (seat in dataRefund.get("seats") as List<String>) {
                            if (seats.contains(seat)) {
                                listSeat.remove(seat)
                            }
                        }

                        val dataSeat = hashMapOf(
                            "date" to date,
                            "seats" to listSeat
                        )

                        db.collection("purchased_seats").document(dataRefund.get("purchased_seats").toString()).collection("time_of_purchase").document(dataRefund.get("seatsTicketRef").toString()).set(dataSeat).addOnSuccessListener {
                            db.collection("users").document(auth.currentUser!!.uid).collection("refund").add(dataRefund).addOnSuccessListener {
                                db.collection("users").document(auth.currentUser!!.uid).collection("transaction").document(IDTransaksi).delete().addOnSuccessListener {
                                    Toast.makeText(this, "Berhasil Refund dalam 1 taun", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }

                    }

                }


            }
        }
//
        if (poster != null && dataTransaksi != null) {
//            _ivPosterFilm.setImageBitmap(poster)
            Glide.with(this).load(poster).into(_ivPosterFilm)
//            Log.d("data transaksi", dataTransaksi.toString())
            val arShowDate = dateFormat2.format(dataTransaksi.show_date).split(" ")

            _tvTanggalTransaksi.setText(dateFormat.format(dataTransaksi.transaction_date))
            _tvJudulFilm.setText(dataTransaksi.judulFilm)
            _tvNamaMallDetail.setText(dataTransaksi.location)
            _tvTanggalShow.setText(arShowDate[0])
            _tvJamShow.setText(arShowDate[1])
            _tvSeats.setText(dataTransaksi.seats.toString())
            _tvStudio.setText(dataTransaksi.studio)
            _tvTotalTicket.setText(dataTransaksi.totalTiket.toString())
            _tvTiketPrice.setText(formatNumberWithCommas(dataTransaksi.tiketPrice))
            _tvTotalOrder.setText(formatNumberWithCommas(dataTransaksi.totalOrder))
            _tvPayment.setText(dataTransaksi.payment)
            _tvTotalPayment.setText(formatNumberWithCommas(dataTransaksi.totalPayment))
            _tvBookingCode.setText(dataTransaksi.booking_code)
            _tvAdmFee.setText(dataTransaksi.admfee)

            val multiFormatWriter = MultiFormatWriter();
            try {
                val bitMatrix = multiFormatWriter.encode(dataTransaksi.booking_code, BarcodeFormat.QR_CODE,400,400);
                val barcodeEncoder = BarcodeEncoder();
                val bitmap = barcodeEncoder.createBitmap(bitMatrix);
                _ivBarcode.setImageBitmap(bitmap);
            } catch (e: WriterException) {
                e.printStackTrace();
            }
            displayAllViews(findViewById(R.id.detailTikerView))
            dismissDialog()
        } else {

            db.collection("users").document(auth.currentUser!!.uid).collection("transaction")
                .document(uidTransaksi.toString()).get().addOnSuccessListener {
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
                        val bitMatrix =
                            multiFormatWriter.encode(bookingCode, BarcodeFormat.QR_CODE, 400, 400);
                        val barcodeEncoder = BarcodeEncoder();
                        val bitmap = barcodeEncoder.createBitmap(bitMatrix);
                        _ivBarcode.setImageBitmap(bitmap);
                    } catch (e: WriterException) {
                        e.printStackTrace();
                    }
//                val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
//                val dateFormat2 = SimpleDateFormat("dd/MM/yyyy HH:mm")

                    val dateshow = dateFormat2.format(showDate).split(" ")

                    Glide.with(this).load(poster).into(_ivPosterFilm)
                    _tvTanggalTransaksi.setText(dateFormat.format(tangglTransaksi))
                    _tvJudulFilm.setText(it.data?.get("judul_film").toString())
                    _tvNamaMallDetail.setText(namaMall)
                    _tvTanggalShow.setText(dateshow[0])
                    _tvJamShow.setText(dateshow[1])
                    _tvSeats.setText(seats.toString())
                    _tvStudio.setText(studio)

                    _tvTotalTicket.setText(totalTiket.toString())
                    _tvTiketPrice.setText(formatNumberWithCommas(tiketPrice))
                    _tvTotalOrder.setText(formatNumberWithCommas(totalOrder))
                    _tvPayment.setText(payment)
                    _tvTotalPayment.setText(formatNumberWithCommas(totalPayment))
                    _tvBookingCode.setText(bookingCode)
                    _tvAdmFee.setText(admFee)

                    displayAllViews(findViewById(R.id.detailTikerView))
                    dismissDialog()

                }.addOnFailureListener {
                    onBackPressed()
                }
            }.addOnFailureListener {
                onBackPressed()
            }
        }
_btnBack.setOnClickListener {
    val intent = Intent(this@detailTiket, Beranda::class.java)
    startActivity(intent)
}


    }

    fun hideAllViews(viewGroup: ViewGroup) {
        for (i in 0 until viewGroup.childCount) {
            val childView: View = viewGroup.getChildAt(i)
            childView.visibility = View.GONE
        }
    }

    fun displayAllViews(viewGroup: ViewGroup) {
        for (i in 0 until viewGroup.childCount) {
            val childView: View = viewGroup.getChildAt(i)
            childView.visibility = View.VISIBLE
        }
    }
}