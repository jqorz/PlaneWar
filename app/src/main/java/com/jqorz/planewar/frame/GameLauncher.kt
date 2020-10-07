package com.jqorz.planewar.frame

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.View.OnLongClickListener
import com.jqorz.planewar.R
import kotlinx.android.synthetic.main.game_launcher.*

class GameLauncher : Activity(), View.OnClickListener, OnLongClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.game_launcher)
        initEvent()
    }


    private fun initEvent() {
        imgBtn_startGame!!.setOnClickListener(this)
        imgBtn_exitGame!!.setOnClickListener(this)
        imgBtn_about!!.setOnClickListener(this)
        imgBtn_setting!!.setOnClickListener(this)
        iv_icon!!.setOnLongClickListener(this)
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.imgBtn_startGame -> {
                this.startActivity(Intent(this, GamePlaying::class.java))
                finish()
            }
            R.id.imgBtn_about -> this.startActivity(Intent(this, GameAbout::class.java))
            R.id.imgBtn_setting -> this.startActivity(Intent(this, GameSetting::class.java))
            R.id.imgBtn_exitGame -> finish()
        }
    }

    override fun onLongClick(view: View): Boolean {
        if (view.id == R.id.iv_icon) {
            val intent = Intent(this, GameCheat::class.java)
            this.startActivity(intent)
        }
        return false
    }
}