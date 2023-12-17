package com.alloys.e_tix.adapterRV

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.alloys.e_tix.R
import com.alloys.e_tix.dataClass.dataTransaksi
import com.alloys.e_tix.detailTiket
import com.bumptech.glide.Glide
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.journeyapps.barcodescanner.BarcodeEncoder
import java.text.SimpleDateFormat

class adapterHistory (
    private val dataTransaksi: dataTransaksi
) : RecyclerView.Adapter<adapterHistory.ListViewHolder>() {

    inner class ListViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        var _posterFilm = itemView.findViewById<ImageView>(R.id.posterFilm)
        var _date = itemView.findViewById<TextView>(R.id.date)
        var _time = itemView.findViewById<TextView>(R.id.time)
        var _movie = itemView.findViewById<TextView>(R.id.movie)
        var _mall = itemView.findViewById<TextView>(R.id.mall)
        var _code = itemView.findViewById<TextView>(R.id.code)
        var _dateShow = itemView.findViewById<TextView>(R.id.dateShow)
        var _timeShow = itemView.findViewById<TextView>(R.id.timeShow)
        var _barcode = itemView.findViewById<Button>(R.id.barcode)
        var _detail = itemView.findViewById<Button>(R.id.detail)
        var context = itemView.context
    }

    private fun showBarcodeDialog(barcode: Bitmap, context: Context) {
        val builder = AlertDialog.Builder(context)
        val imageView = ImageView(context)
        imageView.setImageBitmap(barcode)
        imageView.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        builder.setView(imageView)

        // tambahin close button
        builder.setPositiveButton("Close") { dialog, _ ->
            dialog.dismiss()
        }

        // bikin dialognya
        val dialog = builder.create()
        dialog.show()

        // bikin sak window
        val window = dialog.window
        if (window != null) {
            val layoutParams = WindowManager.LayoutParams()
            layoutParams.copyFrom(window.attributes)
            layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT
            layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT
            window.attributes = layoutParams
        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListViewHolder {
        val view : View = LayoutInflater.from(parent.context)
            .inflate(R.layout.itemhistory,parent,false)
        return ListViewHolder(view)
    }



    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val history = dataTransaksi.arTransaksi[position]
        val poster = dataTransaksi.arPoster[history.poster]

        val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
        val dateFormat2 = SimpleDateFormat("dd/MM/yyyy HH:mm")
        val traansactionDate = dateFormat.format(history.transaction_date).split(" ")
        val dateshow = dateFormat2.format(history.show_date).split(" ")
        holder._date.setText(traansactionDate[0])
        holder._time.setText(traansactionDate[1])
        holder._movie.setText(history.judulFilm)
        holder._mall.setText(history.location)
        holder._code.setText(history.booking_code)
        holder._dateShow.setText(dateshow[0])
        holder._timeShow.setText(dateshow[1])
//        holder._posterFilm.setImageBitmap(poster)
        Glide.with(holder.context).load(poster).into(holder._posterFilm)

        holder._detail.setOnClickListener {
            val intent = Intent(holder.context, detailTiket::class.java).apply {
                putExtra("dataTransaksi", history)
                putExtra("poster", poster)
                putExtra("UIDTransaksi", history.transactionID)
            }
            holder.context.startActivity(intent)
        }

        holder._barcode.setOnClickListener {
            val multiFormatWriter = MultiFormatWriter()
            try {
                val bitMatrix = multiFormatWriter.encode(history.booking_code, BarcodeFormat.QR_CODE,800,800)
                val barcodeEncoder = BarcodeEncoder()
                val bitmap = barcodeEncoder.createBitmap(bitMatrix)
                showBarcodeDialog(bitmap, holder.context)
            } catch (e: WriterException) {
                e.printStackTrace()
            }
        }


    }

    override fun getItemCount(): Int {
        return dataTransaksi.arTransaksi.size
    }
}