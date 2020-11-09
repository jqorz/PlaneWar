package com.jqorz.planewar.frame

import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.view.View
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