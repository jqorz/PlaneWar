package com.jqorz.planewar.frame

import android.app.Activity
import android.os.Bundle
import android.view.View
import com.jqorz.planewar.R

class GameAbout : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.game_about)
    }

    fun mClick(v: View) {
        finish()
    }
}