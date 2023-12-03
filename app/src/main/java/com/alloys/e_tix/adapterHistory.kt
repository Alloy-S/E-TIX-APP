package com.alloys.e_tix

import android.content.Intent
import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.FlexboxLayout
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

class adapterHistory (
    private val listHistory: ArrayList<dataHistory>
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
    ): adapterHistory.ListViewHolder {
        val view : View = LayoutInflater.from(parent.context)
            .inflate(R.layout.historypembelian,parent,false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: adapterHistory.ListViewHolder, position: Int) {
        var history = listHistory[position]

        holder._posterFilm.setImageResource(history.posterFilm)
        holder._date.setText(history.date)
        holder._time.setText(history.time)
        holder._movie.setText(history.movie)
        holder._mall.setText(history.mall)
        holder._code.setText(history.code)
        holder._dateShow.setText(history.dateShow)
        holder._timeShow.setText(history.timeShow)

    }

    override fun getItemCount(): Int {
        return listHistory.size
    }
}