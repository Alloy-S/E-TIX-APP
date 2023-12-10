package com.alloys.e_tix.adapterRV

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.alloys.e_tix.R
import com.alloys.e_tix.dataClass.dataMovie
import com.alloys.e_tix.detail_Film


class movieAdapter(
    private val dataMovie : dataMovie,
) :
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
    ): ListViewHolder {
        val view : View = LayoutInflater.from(parent.context)
            .inflate(R.layout.itemmovie,parent,false)
        return ListViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val movie = dataMovie.arMovie[position]
        val poster = dataMovie.arPoster[movie.urlPoster]


        holder._judul.setText(movie.judul_film)
        holder._durasi.setText(movie.durasi.toString() + " Minutes")
        holder._poster.setImageBitmap(poster)


        holder._poster.setOnClickListener {
            val intent = Intent(it.context, detail_Film::class.java)
            it.context.startActivity(intent)
        }

    }

    override fun getItemCount(): Int {
        return dataMovie.arMovie.size
    }

}