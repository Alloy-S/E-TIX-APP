package com.alloys.e_tix


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.alloys.e_tix.helper.Currency.formatToIDRCurrency
import com.alloys.e_tix.helper.DialogHelper.isDialogVisible
import com.alloys.e_tix.helper.DialogHelper.showDialogBar
import com.alloys.e_tix.helper.DialogHelper.dismissDialog
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.firestore
import java.util.Calendar
import kotlin.random.Random

class selectSeat : AppCompatActivity() {
    val db = Firebase.firestore
    val auth = Firebase.auth
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
        val _btnBack = findViewById<ImageView>(R.id.btnBack)

        val movieId = intent.getStringExtra("movieID")
        val namaMall = intent.getStringExtra("namaMall")
        val tanggal = intent.getStringExtra("tanggal")
        val waktuMulai = intent.getStringExtra("waktuMulai")
        val hargaTiket = intent.getIntExtra("hargaTiket", 0)
        val purchased_seatsRef = intent.getStringExtra("seats")

        _btnBack.setOnClickListener {
            onBackPressed()
        }

        _tvNamaMall.setText(namaMall)
        _tvtanggal.setText("$tanggal | $waktuMulai")

        val totalBaris = 15
        val totalKolom = 17
        val frameLayoutSize = resources.getDimensionPixelSize(R.dimen.frame_size_seat) // Replace with your desired frame size in pixels
        val frameLayoutParams = LinearLayout.LayoutParams(frameLayoutSize, frameLayoutSize)
        frameLayoutParams.rightMargin = resources.getDimensionPixelSize(R.dimen.frame_margin_seat) // Replace with your desired margin in pixels

        var kolomTitle = 'A'

        val calendar = Calendar.getInstance()
//        calendar.set(Calendar.HOUR_OF_DAY, 0)
//        calendar.set(Calendar.MINUTE, 0)
//        calendar.set(Calendar.SECOND, 0)
//        val startOfDay = calendar.timeInMillis
//
//        calendar.set(Calendar.HOUR_OF_DAY, 23)
//        calendar.set(Calendar.MINUTE, 59)
//        calendar.set(Calendar.SECOND, 59)
//        val endOfDay = calendar.timeInMillis
//
//        val startTimestamp = Timestamp(Date(startOfDay))
//        val endTimestamp = Timestamp(Date(endOfDay))

        _btnConfirm.setOnClickListener {


            if (purchased_seatsRef != null && selectedSeat.isNotEmpty()) {
                showDialogBar(this, "Process....")
                val isDialogVisible = isDialogVisible()
                val purchasedSeatRef =   db.collection("purchased_seats").document(purchased_seatsRef)

                db.runTransaction { transaction ->
                    val snapshot = transaction.get(purchasedSeatRef)

                    val seats = snapshot.get("seats") as List<String>
                    var available = true
                    Log.d("CEK SEATS", seats.toString())
                    for (seat in selectedSeat) {
                        if (seats.contains(seat)) {
                            available = false
                        }
                    }

                    if (available) {
                        val userID = auth.currentUser?.uid
                        val charPool : List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9') // Define the character pool
                        val randomString = (1..6)
                            .map { Random.nextInt(0, charPool.size) }
                            .map(charPool::get)
                            .joinToString("")
                        val currentTimestamp = calendar.timeInMillis
                        val dataShowtime = waktuMulai?.split(":")
                        calendar.set(Calendar.HOUR_OF_DAY, dataShowtime!!.get(0).toInt())
                        calendar.set(Calendar.MINUTE, dataShowtime!!.get(1).toInt())
                        val showtime = calendar.timeInMillis
                        println("Random string of 6 characters: $randomString")


                        val dataTransaksi = hashMapOf(
                            "transaction_date" to currentTimestamp,
                            "movieId" to movieId,
                            "location" to namaMall,
                            "booking_code" to randomString,
                            "harga_tiket" to hargaTiket,
                            "studio" to 1,
                            "payment" to "Free",
                            "seats" to selectedSeat,
                            "total_tiket" to selectedSeat.size,
                            "total_order" to (selectedSeat.size * hargaTiket) + (selectedSeat.size * 2500),
                            "show_date" to showtime,
                            "admFee" to 2500
                        )

                        db.collection("users").document(userID!!).collection("transaction").add(dataTransaksi).addOnSuccessListener {
                            for (seat in selectedSeat) {
                                purchasedSeat.add(seat)
                            }

                            val uidTransaksi = it.id
                            db.collection("purchased_seats").document(purchased_seatsRef).update("seats", purchasedSeat).addOnSuccessListener {
                                dismissDialog()
                                val intent = Intent(this, detailTiket::class.java).apply {
                                    putExtra("UIDTransaksi", uidTransaksi)
                                }
                                finish()
                                this.startActivity(intent)
                            }
                        }
                    } else {
                        throw FirebaseFirestoreException(
                            "Kursi sudah Terbeli",
                            FirebaseFirestoreException.Code.ABORTED,
                        )
                    }
                }


            }
        }


        Log.d("input to DB", "$movieId $waktuMulai")
        if (purchased_seatsRef != null) {
            db.collection("purchased_seats").document(purchased_seatsRef).get().addOnSuccessListener {
                Log.d("ISI SEATS", it.data?.get("seats").toString())
                for (seat in it.data?.get("seats") as List<String>) {
                    purchasedSeat.add(seat)
                }

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

            }.addOnFailureListener {
                onBackPressed()
            }
        } else {
            onBackPressed()
        }


//        purchasedSeat.add("A2")
//        purchasedSeat.add("A3")
//        purchasedSeat.add("D8")
//        purchasedSeat.add("D9")
//        purchasedSeat.add("D10")
//        purchasedSeat.add("D11")
//        purchasedSeat.add("D7")
//        purchasedSeat.add("C9")
//        purchasedSeat.add("C10")
//        purchasedSeat.add("C11")
//        purchasedSeat.add("C7")






    }
}