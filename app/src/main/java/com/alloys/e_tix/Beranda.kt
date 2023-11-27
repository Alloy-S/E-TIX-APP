package com.alloys.e_tix

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView

class Beranda : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_beranda)

        //Fragment awal
        val mFragmentManager = supportFragmentManager
        val mfMovie = movieFragment()
        val bundle = Bundle()

        mfMovie.arguments = bundle
        mFragmentManager.findFragmentByTag(movieFragment::class.java.simpleName)
        mFragmentManager.beginTransaction().apply {
            replace(R.id.frameContainer, mfMovie, movieFragment::class.java.simpleName)
            commit()


            findViewById<ImageView>(R.id.ivMovie).setOnClickListener {
                mfMovie.arguments = bundle
                mFragmentManager.findFragmentByTag(movieFragment::class.java.simpleName)
                mFragmentManager.beginTransaction().apply {
                    replace(R.id.frameContainer, mfMovie, movieFragment::class.java.simpleName)
                    commit()
                }
            }

            findViewById<ImageView>(R.id.ivProfile).setOnClickListener {
                val mfProfile = profileFragment()
                mfProfile.arguments = bundle
                mFragmentManager.findFragmentByTag(profileFragment::class.java.simpleName)
                mFragmentManager.beginTransaction().apply {
                    replace(R.id.frameContainer, mfProfile, profileFragment::class.java.simpleName)
                    commit()
                }
            }
            findViewById<ImageView>(R.id.ivUpcoming).setOnClickListener {
                val mfUpcoming = upcomingFragment()
                mfUpcoming.arguments = bundle
                mFragmentManager.findFragmentByTag(upcomingFragment::class.java.simpleName)
                mFragmentManager.beginTransaction().apply {
                    replace(R.id.frameContainer, mfUpcoming, upcomingFragment::class.java.simpleName)
                    commit()
                }
            }
        }

        }
    companion object {
        const val dataTerima = "GETDATA"
    }
}