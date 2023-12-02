package com.alloys.e_tix

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView

class adminPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_page)

        //Fragment awal
        val mFragmentManager = supportFragmentManager
        val mfMovieAdmin = fragmentMovieAdmin()
        val bundle = Bundle()

        mfMovieAdmin.arguments = bundle
        mFragmentManager.findFragmentByTag(fragmentMovieAdmin::class.java.simpleName)
        mFragmentManager.beginTransaction().apply {
            replace(R.id.adminPage, mfMovieAdmin, fragmentMovieAdmin::class.java.simpleName)
            commit()

            //Fragment movie
            findViewById<ImageView>(R.id.ivMovie).setOnClickListener {
                mfMovieAdmin.arguments = bundle
                mFragmentManager.findFragmentByTag(fragmentMovieAdmin::class.java.simpleName)
                mFragmentManager.beginTransaction().apply {
                    replace(R.id.adminPage, mfMovieAdmin, fragmentMovieAdmin::class.java.simpleName)
                    commit()
                }
            }
            //Fragment Profile
            findViewById<ImageView>(R.id.ivProfile).setOnClickListener {
                val mfProfile = fragmentProfileAdmin()
                mfProfile.arguments = bundle
                mFragmentManager.findFragmentByTag(fragmentProfileAdmin::class.java.simpleName)
                mFragmentManager.beginTransaction().apply {
                    replace(R.id.adminPage, mfProfile, fragmentProfileAdmin::class.java.simpleName)
                    commit()
                }
            }
            //Fragment Add Movie
            findViewById<ImageView>(R.id.ivAddMovie).setOnClickListener {
                val mfAddMovie = fragmentAddMovieAdmin()
                mfAddMovie.arguments = bundle
                mFragmentManager.findFragmentByTag(fragmentAddMovieAdmin::class.java.simpleName)
                mFragmentManager.beginTransaction().apply {
                    replace(R.id.adminPage, mfAddMovie, fragmentAddMovieAdmin::class.java.simpleName)
                    commit()
                }
            }
        }
    }
}