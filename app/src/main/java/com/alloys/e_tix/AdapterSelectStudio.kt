package com.alloys.e_tix

import android.os.Parcel
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
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
        val formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)
        val formatted = current.format(formatter)

        holder._nama.setText(mall.namaMall)
        holder._tanggal.setText(formatted)
        holder._harga.setText("Rp.25.000")
        val dpToPixels = holder.context.resources.displayMetrics.density
        for (time in mall.arWaktu) {

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
            textView.text = time

            frameLayout.addView(textView)

            frameLayout.setOnClickListener {

            }
            holder._listWaktu.addView(frameLayout)
        }

    }
}