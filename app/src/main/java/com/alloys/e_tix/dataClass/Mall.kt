package com.alloys.e_tix.dataClass

import android.os.Parcel
import android.os.Parcelable


data class Mall(
    val mallID: String?,
    val namaMall: String?,
    val showtime: ArrayList<jadwalFilm>,
    val hargaTiket: Int,
    val tmpMovieID: String?,
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        ArrayList(),
        parcel.readInt(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(mallID)
        parcel.writeString(namaMall)
        parcel.writeInt(hargaTiket)
        parcel.writeString(tmpMovieID)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Mall> {
        override fun createFromParcel(parcel: Parcel): Mall {
            return Mall(parcel)
        }

        override fun newArray(size: Int): Array<Mall?> {
            return arrayOfNulls(size)
        }
    }
}
