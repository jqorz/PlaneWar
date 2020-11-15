package com.jqorz.planewar.frame

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.view.View
import android.view.View.OnLongClickListener
import com.jqorz.planewar.R
import com.jqorz.planewar.tools.UserDataManager
import kotlinx.android.synthetic.main.game_launcher.*

class GameLauncher : FragmentActivity(), View.OnClickListener, OnLongClickListener {
    private lateinit var mSettingDialog: GameSettingDialog

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
            R.id.imgBtn_setting -> {
                showSettingDialog()
            }
            R.id.imgBtn_exitGame -> finish()
        }
    }

    fun showSettingDialog() {
        mSettingDialog = GameSettingDialog(this)
        mSettingDialog.show()
        mSettingDialog.setSwitch(UserDataManager.isOpenMusic(), UserDataManager.isOpenSound())
        mSettingDialog.callback = object : GameSettingDialog.DialogCallback {
            override fun onSwitchSound(open: Boolean) {
                UserDataManager.setOpenSound(open)
            }

            override fun onSwitchMusic(open: Boolean) {
                UserDataManager.setOpenMusic(open)
            }
        }
    }

    override fun onLongClick(view: View): Boolean {
        if (view.id == R.id.iv_icon) {
            GameCheat.start(this)
        }
        return true
    }

    companion object {
        @JvmStatic
        fun start(context: Context) {
            val starter = Intent(context, GameLauncher::class.java)
            context.startActivity(starter)
        }
    }
}