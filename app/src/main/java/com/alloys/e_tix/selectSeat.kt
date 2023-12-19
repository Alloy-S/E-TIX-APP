package com.alloys.e_tix


import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.alloys.e_tix.dataClass.Mall
import com.alloys.e_tix.helper.Currency.formatToIDRCurrency
import com.alloys.e_tix.helper.DialogHelper.dismissDialog
import com.alloys.e_tix.helper.DialogHelper.isDialogVisible
import com.alloys.e_tix.helper.DialogHelper.showDialogBar
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.firestore
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.UUID
import kotlin.random.Random

class selectSeat : AppCompatActivity() {
    val db = Firebase.firestore
    val auth = Firebase.auth
    var selectedSeat = ArrayList<String>()
    var purchasedSeat = ArrayList<String>()
    var startOfDay: Long = 0
    var endOfDay: Long = 0
    lateinit var purchasedTicketTimeRef: String
    lateinit var _btnConfirm : Button
    lateinit var _llSeat : LinearLayout
    lateinit var _tvTotalTiket: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_seat)


        _llSeat = findViewById<LinearLayout>(R.id.llSeat)
        val _tvNamaMall = findViewById<TextView>(R.id.tvNamaMallSeat)
        val _tvtanggal = findViewById<TextView>(R.id.tvtanggalPenayangan)
        _tvTotalTiket = findViewById<TextView>(R.id.tvTotalTiket)
        _btnConfirm = findViewById<Button>(R.id.btnConfirm)
        val _btnBack = findViewById<ImageView>(R.id.btnBack)

        val movieId = intent.getStringExtra("movieID")
        val namaMall = intent.getStringExtra("namaMall")
        val tanggal = intent.getStringExtra("tanggal")
        val waktuMulai = intent.getStringExtra("waktuMulai")
        val hargaTiket = intent.getIntExtra("hargaTiket", 0)
        val purchased_seatsRef = intent.getStringExtra("seats")

        val dataMall = intent.getParcelableExtra("dataMall", Mall::class.java)

        _btnBack.setOnClickListener {
            onBackPressed()
        }

        _tvNamaMall.setText(namaMall)
        _tvtanggal.setText("$tanggal | $waktuMulai")

        val totalBaris = 15
        val totalKolom = 17


        val calendar = Calendar.getInstance()

        _btnConfirm.isEnabled = false

        _btnConfirm.setOnClickListener {
            if (purchased_seatsRef != null && selectedSeat.isNotEmpty()) {
                showDialogBar(this, "Process....")
                val isDialogVisible = isDialogVisible()
                val purchasedSeatRef =   db.collection("purchased_seats").document(purchased_seatsRef).collection("time_of_purchase").document(purchasedTicketTimeRef)

                db.runTransaction { transaction ->
                    val snapshot = transaction.get(purchasedSeatRef)

                    val seats = snapshot.get("seats") as List<String>
                    var available = true
                    Log.d("CEK SEATS", seats.toString())
                    for (seat in selectedSeat) {
                        Log.d("CEK", seat.toString())
                        if (seats.contains(seat)) {
                            available = false
                        }
                    }

                    if (available) {

                        Log.d("ADDING TRANSACTION", dataMall.toString())
                        Log.d("ADDING TRANSACTION", "PROSSES 1")
                        val userID = auth.currentUser?.uid
                        Log.d("ADDING TRANSACTION", "PROSSES 2")
                        val charPool : List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9') // Define the character pool
                        Log.d("ADDING TRANSACTION", "PROSSES 3")
                        val randomString = UUID.randomUUID().toString().replace("-","").substring(0, 6)

                        Log.d("ADDING TRANSACTION", "PROSSES 4")
                        val currentTimestamp = calendar.timeInMillis
                        Log.d("ADDING TRANSACTION", "PROSSES 5")
                        val dataShowtime = waktuMulai?.split(":")
                        Log.d("ADDING TRANSACTION", "PROSSES 6")
                        calendar.set(Calendar.HOUR_OF_DAY, dataShowtime!!.get(0).toInt())
                        Log.d("ADDING TRANSACTION", "PROSSES 7")
                        calendar.set(Calendar.MINUTE, dataShowtime!!.get(1).toInt())
                        Log.d("ADDING TRANSACTION", "PROSSES 8")
                        val showtime = calendar.timeInMillis
//                        println("Random string of 6 characters: $randomString")


                        Log.d("ADDING TRANSACTION", "PROSSES 9")
                        val dataTransaksi = hashMapOf(
                            "transaction_date" to System.currentTimeMillis(),
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
                            "admFee" to 2500,
                            "seatsTicketRef" to purchasedTicketTimeRef,
                            "purchased_seats" to purchased_seatsRef
                        )
                        Log.d("Make Transaction data", dataTransaksi.toString())

                        db.collection("users").document(userID!!).collection("transaction").add(dataTransaksi).addOnSuccessListener {
                            for (seat in selectedSeat) {
                                purchasedSeat.add(seat)
                            }

                            val uidTransaksi = it.id
                            db.collection("purchased_seats").document(purchased_seatsRef).collection("time_of_purchase").document(purchasedTicketTimeRef).update("seats", purchasedSeat).addOnSuccessListener {
                                dismissDialog()
                                val intent = Intent(this, detailTiket::class.java).apply {
                                    putExtra("UIDTransaksi", uidTransaksi)
//                                    putExtra("dataTransaksi", dataTransaksi)
                                    putExtra("poster", dataMall!!.tmpPoster)
                                }
                                finish()
                                startActivity(intent)
                            }.addOnFailureListener {
                                Log.d("GAGAL ADD purchased seats", it.message.toString())
                            }
                        }.addOnFailureListener {
                            Log.d("GAGAL ADD TRANSAKSI", it.message.toString())
                        }
                    } else {
                        val alertDialog = AlertDialog.Builder(this)
                            .setTitle("Transaksi Gagal")
                            .setMessage("Kursi sudaah dibeli")
                            .setPositiveButton(
                                "Ok",
                                DialogInterface.OnClickListener { dialog, which ->
                                    dialog.dismiss()
                                }
                            )
                        alertDialog.show()
                        throw FirebaseFirestoreException(
                            "Kursi sudah Terbeli",
                            FirebaseFirestoreException.Code.ABORTED,
                        )


                    }
                }


            }
        }

//        calendar.set(Calendar.DAY_OF_MONTH, 16)


//        val startTimestamp = Timestamp(Date(startOfDay))
//        val endTimestamp = Timestamp(Date(endOfDay))

        Log.d("input to DB", "$movieId $waktuMulai")
        if (purchased_seatsRef != null) {
            calendar.set(Calendar.HOUR_OF_DAY, 0)
            calendar.set(Calendar.MINUTE, 0)
            calendar.set(Calendar.SECOND, 0)
            startOfDay = calendar.timeInMillis

            calendar.set(Calendar.HOUR_OF_DAY, 23)
            calendar.set(Calendar.MINUTE, 59)
            calendar.set(Calendar.SECOND, 59)
            endOfDay = calendar.timeInMillis
            Log.d("query timestamp", "$startOfDay $endOfDay")
            val query = db.collection("purchased_seats").document(purchased_seatsRef).collection("time_of_purchase").whereGreaterThanOrEqualTo("date", startOfDay).whereLessThanOrEqualTo("date", endOfDay)

            query.get().addOnSuccessListener { result ->
                Log.d("query", result.query.toString())
                Log.d("query", result.toString())

                if (result.size() == 0) {
//                    Toast.makeText(this, "Not Found", Toast.LENGTH_SHORT).show()
//                    finish()
                    val calendar2 = Calendar.getInstance()
                    Log.d("Date calender", getDate(calendar.timeInMillis, "dd/MM/yyyy hh:mm:ss.SSS").toString())
                    val dataPurchasedSeats = hashMapOf(
                        "date" to System.currentTimeMillis(),
                        "seats" to ArrayList<String>()
                    )
                    db.collection("purchased_seats").document(purchased_seatsRef).collection("time_of_purchase").add(dataPurchasedSeats).addOnSuccessListener {
                        purchasedTicketTimeRef = it.id
                        prepareSeatView(totalBaris, totalKolom, hargaTiket)
                    }
                } else {
                    for (item in result) {
                        Log.d("result query", item.data.toString())
                        purchasedTicketTimeRef = item.id
                        Log.d("ISI SEATS", item.data?.get("seats").toString())
                        for (seat in item.data?.get("seats") as List<String>) {
                            purchasedSeat.add(seat)
                        }

                        prepareSeatView(totalBaris, totalKolom, hargaTiket)
                    }
                }


            }.addOnFailureListener {
                Toast.makeText(this, "Terjadi kesalahan", Toast.LENGTH_SHORT).show()
                onBackPressed()
            }
        } else {
            Toast.makeText(this, "Terjadi kesalahan", Toast.LENGTH_SHORT).show()
            onBackPressed()
        }

    }

    fun getDate(milliSeconds: Long, dateFormat: String?): String? {
        // Create a DateFormatter object for displaying date in specified format.
        val formatter = SimpleDateFormat(dateFormat)

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = milliSeconds
        return formatter.format(calendar.time)
    }

    fun prepareSeatView(totalBaris: Int, totalKolom: Int, hargaTiket: Int) {

        val frameLayoutSize = resources.getDimensionPixelSize(R.dimen.frame_size_seat) // Replace with your desired frame size in pixels
        val frameLayoutParams = LinearLayout.LayoutParams(frameLayoutSize, frameLayoutSize)
        frameLayoutParams.rightMargin = resources.getDimensionPixelSize(R.dimen.frame_margin_seat) // Replace with your desired margin in pixels

        var kolomTitle = 'A'
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


                            _btnConfirm.isEnabled = selectedSeat.size != 0
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
}