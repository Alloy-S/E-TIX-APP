package com.alloys.e_tix

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class movieAdapter(private val listMovie : ArrayList<dataMovie>) :
    RecyclerView.Adapter<movieAdapter.ListViewHolder>() {

    inner class ListViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
//        var _poster = itemView.findViewById<ImageView>(R.id.ivPoster)
        var _judul = itemView.findViewById<TextView>(R.id.tvJudul)
        var _durasi = itemView.findViewById<TextView>(R.id.tvDurasiFilm)
        var _poster = itemView.findViewById<ImageView>(R.id.ivPoster)
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): movieAdapter.ListViewHolder {
        val view : View = LayoutInflater.from(parent.context)
            .inflate(R.layout.itemmovie,parent,false)
        return ListViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val movie = listMovie[position]
//        holder._poster.setImageResource(movie.urlPoster.toInt())
        holder._judul.setText(movie.judul_film)
        holder._durasi.setText(movie.durasi + " Minutes")
        Glide.with(holder.itemView.context)
            .load(movie.urlPoster)
            .into(holder._poster)
    }

    override fun getItemCount(): Int {
        return listMovie.size
    }

}