package com.jqorz.planewar.frame

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.widget.CompoundButton
import com.jqorz.planewar.R
import kotlinx.android.synthetic.main.dialog_setting.*

class GameSettingDialog(context: Context) : AlertDialog(context), CompoundButton.OnCheckedChangeListener {
    var callback: DialogCallback? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_setting)
        initView()
    }


    fun setSwitch(openMusic: Boolean, openSound: Boolean) {
        swt_Music!!.isChecked = openMusic
        swt_Sound!!.isChecked = openSound
    }


    private fun initView() {
        swt_Sound!!.setOnCheckedChangeListener(this)
        swt_Music!!.setOnCheckedChangeListener(this)
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
    }


}