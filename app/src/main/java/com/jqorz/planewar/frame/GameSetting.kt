package com.jqorz.planewar.frame

import android.app.Activity
import android.os.Bundle
import android.widget.CompoundButton
import com.jqorz.planewar.R
import com.jqorz.planewar.tools.UserDataManager
import kotlinx.android.synthetic.main.game_setting.*

class GameSetting : Activity(), CompoundButton.OnCheckedChangeListener {

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