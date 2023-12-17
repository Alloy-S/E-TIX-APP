package com.alloys.e_tix.adapterRV

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.alloys.e_tix.R
import com.alloys.e_tix.dataClass.dataMovie
import com.alloys.e_tix.detail_Film
import com.bumptech.glide.Glide

class adapterUpcoming (
    private val dataMovieUpcoming : dataMovie,
) :
    RecyclerView.Adapter<adapterUpcoming.ListViewHolder>() {
    inner class ListViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        //        var _poster = itemView.findViewById<ImageView>(R.id.ivPoster)
        var _judulUpcoming = itemView.findViewById<TextView>(R.id.tvJudulUpcoming)
        var _durasiUpcoming = itemView.findViewById<TextView>(R.id.tvDurasiFilmUpcoming)
        var _posterUpcoming = itemView.findViewById<ImageView>(R.id.ivPosterUpcoming)
        var _itemUpcoming = itemView.findViewById<ConstraintLayout>(R.id.itemUpcoming)
        val context = itemView.context

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): adapterUpcoming.ListViewHolder {
        val view : View = LayoutInflater.from(parent.context)
            .inflate(R.layout.itemupcoming,parent,false)
        return ListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataMovieUpcoming.arMovie.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: adapterUpcoming.ListViewHolder, position: Int) {
        val movie = dataMovieUpcoming.arMovie[position]
        val poster = dataMovieUpcoming.arPoster[movie.urlPoster]

        holder._judulUpcoming.setText(movie.judul_film)
        holder._durasiUpcoming.setText(movie.durasi + " Minutes")
        Glide.with(holder.context).load(poster).into(holder._posterUpcoming)
        holder._itemUpcoming.setOnClickListener {
            val intent = Intent(it.context, detail_Film::class.java).apply {
                putExtra("movieID", movie.movieID)
                putExtra("dataMovie", movie)
                putExtra("posterMovie", poster)
            }
            it.context.startActivity(intent)
        }

    }

}
