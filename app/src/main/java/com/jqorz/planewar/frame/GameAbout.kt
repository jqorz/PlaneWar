package com.jqorz.planewar.frame

import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentActivity
import com.jqorz.planewar.R

class GameAbout : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.game_about)
    }

    fun mClick(v: View) {
        finish()
    }
}