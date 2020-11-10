package com.jqorz.planewar.frame

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.CompoundButton
import com.jqorz.planewar.R
import kotlinx.android.synthetic.main.dialog_setting.*

class GamePauseDialog(context: Context) : Dialog(context), CompoundButton.OnCheckedChangeListener, View.OnClickListener {
    var callback: DialogCallback? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_setting)
        initView()
    }

    fun onlyShowSetting(onlyShowSetting: Boolean) {
        if (onlyShowSetting) {
            imgBtn_Pause.visibility = View.GONE
            tv_exit_game.visibility = View.GONE
        } else {
            imgBtn_Pause.visibility = View.VISIBLE
            tv_exit_game.visibility = View.VISIBLE
        }
    }

    fun setSwitch(openMusic: Boolean, openSound: Boolean) {
        swt_Music!!.isChecked = openMusic
        swt_Sound!!.isChecked = openSound
    }


    private fun initView() {
        swt_Sound!!.setOnCheckedChangeListener(this)
        swt_Music!!.setOnCheckedChangeListener(this)
        imgBtn_Pause.setOnClickListener(this)
    }


    override fun onCheckedChanged(compoundButton: CompoundButton, b: Boolean) {
        when (compoundButton.id) {
            R.id.swt_Sound -> callback?.onSwitchSound(b)
            R.id.swt_Music -> callback?.onSwitchMusic(b)
        }
    }

    interface DialogCallback {
        fun onSwitchSound(open: Boolean)
        fun onSwitchMusic(open: Boolean)
        fun onPauseOrResume()
        fun onExitGame()
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.imgBtn_Pause -> {
                callback?.onPauseOrResume()
            }
            R.id.tv_exit_game -> {
                callback?.onExitGame()
            }
        }
    }
}