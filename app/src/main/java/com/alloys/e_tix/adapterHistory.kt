package com.alloys.e_tix

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

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
            .inflate(R.layout.itemhistory,parent,false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: adapterHistory.ListViewHolder, position: Int) {
        var history = listHistory[position]



//        holder._posterFilm.setImageResource(history.movieId)
        holder._date.setText(history.transaction_date)
        holder._time.setText(history.transaction_date)
        holder._movie.setText(history.movieId)
        holder._mall.setText(history.location)
        holder._code.setText(history.booking_code)
        holder._dateShow.setText(history.show_date)
        holder._timeShow.setText(history.show_date)

    }

    override fun getItemCount(): Int {
        return listHistory.size
    }
}