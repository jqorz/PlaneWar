package com.jqorz.planewar.frame

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.View.OnLongClickListener
import android.widget.ImageView
import com.jqorz.planewar.R

class GameLauncher : Activity(), View.OnClickListener, OnLongClickListener {
    private var imgBtn_startGame: ImageView? = null
    private var imgBtn_exitGame: ImageView? = null
    private var imgBtn_about: ImageView? = null
    private var imgBtn_setting: ImageView? = null
    private var iv_icon: ImageView? = null
    override fun onCreate(savedInstanceState: Bundle) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.game_launcher)
        initView()
        initEvent()
    }

    private fun initView() {
        imgBtn_startGame = findViewById<View>(R.id.imgBtn_startGame) as ImageView
        imgBtn_exitGame = findViewById<View>(R.id.imgBtn_exitGame) as ImageView
        imgBtn_about = findViewById<View>(R.id.imgBtn_about) as ImageView
        imgBtn_setting = findViewById<View>(R.id.imgBtn_Setting) as ImageView
        iv_icon = findViewById<View>(R.id.iv_icon) as ImageView
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
            R.id.imgBtn_Setting -> this.startActivity(Intent(this, GameSetting::class.java))
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