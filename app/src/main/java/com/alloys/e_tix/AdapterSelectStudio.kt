package com.alloys.e_tix

import android.content.Intent
import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.alloys.e_tix.helper.Currency
import com.google.android.flexbox.FlexboxLayout
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

class AdapterSelectStudio (
    private val listMall: ArrayList<Mall>
) : RecyclerView.Adapter<AdapterSelectStudio.ListViewHolder>() {

    inner class ListViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        var _nama = itemView.findViewById<TextView>(R.id.tvNamaMall)
        var _tanggal = itemView.findViewById<TextView>(R.id.tvTanggal)
        var _harga = itemView.findViewById<TextView>(R.id.tvHarga)
        var _listWaktu = itemView.findViewById<FlexboxLayout>(R.id.lvListWaktu)
        var context = itemView.context
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AdapterSelectStudio.ListViewHolder {
        val view : View = LayoutInflater.from(parent.context).inflate(R.layout.itemstudio, parent, false)
        return ListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listMall.size
    }

    override fun onBindViewHolder(holder: AdapterSelectStudio.ListViewHolder, position: Int) {
        var mall = listMall[position]

        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("MMM d, yyyy")
        val formatted = current.format(formatter)
        val dateTime = formatted.split(" ")

        holder._nama.setText(mall.namaMall)
        holder._tanggal.setText(formatted)

        val hargaInIDR = Currency.formatToIDRCurrency(mall.hargaTiket)
        holder._harga.setText(hargaInIDR)
        val dpToPixels = holder.context.resources.displayMetrics.density
        Log.d("NAMA MALL", mall.namaMall.toString())
//        Log.d("isi SHOWTIME", mall.showtime[position].toString()
        for (time in mall.showtime) {

            val frameLayout = FrameLayout(holder.context)
            val layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )

            layoutParams.rightMargin = (10 * dpToPixels).toInt()
            layoutParams.bottomMargin = (10 * dpToPixels).toInt()
            frameLayout.layoutParams = layoutParams
            frameLayout.setBackgroundResource(R.drawable.layout_border)

            val textView = TextView(holder.context)
            val textParams = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
            )

            val margin = (6 * dpToPixels).toInt()

            textParams.setMargins(margin, margin, margin, margin)
            textView.layoutParams = textParams
            textView.text = time.showtime

            frameLayout.addView(textView)

            frameLayout.setOnClickListener {
                val intent = Intent(holder.context, selectSeat::class.java).apply {
                    putExtra("movieID", mall.tmpMovieID)
                    putExtra("namaMall", mall.namaMall)
                    putExtra("tanggal", formatted.toString())
                    putExtra("hargaTiket", 25000)
                    putExtra("waktuMulai", time.showtime)
                    putExtra("seats", time.purchased_seat)
                }
                holder.context.startActivity(intent)
            }
            holder._listWaktu.addView(frameLayout)
        }

    }
}