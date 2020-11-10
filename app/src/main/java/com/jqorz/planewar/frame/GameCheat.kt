package com.jqorz.planewar.frame

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import com.jqorz.planewar.R
import com.jqorz.planewar.constant.ConstantValue
import com.jqorz.planewar.utils.ToastUtil
import kotlinx.android.synthetic.main.game_cheat.*

class GameCheat(context: Context) : Dialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.game_cheat)
    }


    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            val s = "jqorz_"
            when (edtTxt_cheat!!.text.toString()) {
                s + ConstantValue.CHEAT_STATE_1 -> { //初始炸弹数为10
                    ConstantValue.CHEAT_CURRENT_STATE = ConstantValue.CHEAT_STATE_1
                    ToastUtil.showToast("Success_炸弹狂魔")
                    dismiss()
                }
                s + ConstantValue.CHEAT_STATE_2 -> { //子弹补给时间缩短10s
                    ConstantValue.CHEAT_CURRENT_STATE = ConstantValue.CHEAT_STATE_2
                    ToastUtil.showToast("Success_蓝色风暴")
                    dismiss()
                }
                s + ConstantValue.CHEAT_STATE_3 -> { //敌机分数每个加100
                    ConstantValue.CHEAT_CURRENT_STATE = ConstantValue.CHEAT_STATE_3
                    ToastUtil.showToast("Success_得分高手")
                    dismiss()
                }
            }
        }
        return super.onKeyDown(keyCode, event)
    }

    companion object{
        @JvmStatic
        fun start(context: Context) {
            val starter = Intent(context, GameCheat::class.java)
            context.startActivity(starter)
        }
    }
}