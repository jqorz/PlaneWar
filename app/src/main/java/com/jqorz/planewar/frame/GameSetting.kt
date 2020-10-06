package com.jqorz.planewar.frame

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.CompoundButton
import android.widget.Switch
import com.jqorz.planewar.R
import com.jqorz.planewar.tools.UserDataManager

class GameSetting : Activity(), CompoundButton.OnCheckedChangeListener {
    private var swt_Sound: Switch? = null
    private var swt_Music: Switch? = null
    override fun onCreate(savedInstanceState: Bundle) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.game_setting)
        initView()
        initEvent()
        setSwitch()
    }

    private fun setSwitch() {
        swt_Music!!.isChecked = UserDataManager.isOpenMusic()
        swt_Sound!!.isChecked = UserDataManager.isOpenSound()
    }

    private fun initView() {
        swt_Sound = findViewById<View>(R.id.swt_Sound) as Switch
        swt_Music = findViewById<View>(R.id.swt_Music) as Switch
    }

    private fun initEvent() {
        swt_Sound!!.setOnCheckedChangeListener(this)
        swt_Music!!.setOnCheckedChangeListener(this)
    }

    override fun onCheckedChanged(compoundButton: CompoundButton, b: Boolean) {
        when (compoundButton.id) {
            R.id.swt_Sound -> UserDataManager.setOpenSound(b)
            R.id.swt_Music -> UserDataManager.setOpenMusic(b)
        }
    }
}