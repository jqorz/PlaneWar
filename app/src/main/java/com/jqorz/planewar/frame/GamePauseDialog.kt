package com.jqorz.planewar.frame

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.View
import com.jqorz.planewar.R
import kotlinx.android.synthetic.main.dialog_pause.*

class GamePauseDialog(context: Context) : AlertDialog(context), View.OnClickListener {
    var callback: DialogCallback? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_pause)
        initView()
    }


    private fun initView() {
        imgBtn_Pause.setOnClickListener(this)
        tv_exit_game.setOnClickListener(this)
        tv_back_home.setOnClickListener(this)
    }


    interface DialogCallback {
        fun onPauseOrResume()
        fun onExitGame()
        fun onBackHome()
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.imgBtn_Pause -> {
                callback?.onPauseOrResume()
            }
            R.id.tv_exit_game -> {
                callback?.onExitGame()
            }
            R.id.tv_back_home -> {
                callback?.onBackHome()
            }
        }
    }
}