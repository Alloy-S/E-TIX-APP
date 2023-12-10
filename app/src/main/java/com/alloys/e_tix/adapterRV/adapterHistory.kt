package com.alloys.e_tix.adapterRV

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.alloys.e_tix.R
import com.alloys.e_tix.dataClass.dataHistory
import com.alloys.e_tix.dataClass.dataTransaksi
import com.alloys.e_tix.detailTiket
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
        val dateshow = dateFormat2.format(history.transaction_date).split(" ")
        holder._date.setText(traansactionDate[0])
        holder._time.setText(traansactionDate[1])
        holder._movie.setText(history.judulFilm)
        holder._mall.setText(history.location)
        holder._code.setText(history.booking_code)
        holder._dateShow.setText(dateshow[0])
        holder._timeShow.setText(dateshow[1])
        holder._posterFilm.setImageBitmap(poster)

        holder._detail.setOnClickListener {
            val intent = Intent(holder.context, detailTiket::class.java).apply {
                putExtra("dataTransaksi", dataHistory::class.java)
                putExtra("poster", poster)
                putExtra("UIDTransaksi", history.transactionID)
            }
            holder.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return dataTransaksi.arTransaksi.size
    }
}