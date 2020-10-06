package com.jqorz.planewar.frame

import android.app.Activity
import android.os.Bundle
import android.view.KeyEvent
import android.widget.Toast
import com.jqorz.planewar.R
import com.jqorz.planewar.constant.ConstantValue
import kotlinx.android.synthetic.main.game_cheat.*

class GameCheat : Activity() {

    override fun onCreate(savedInstanceState: Bundle) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.game_cheat)
    }


    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            val s = "jqorz_"
            val ed = edtTxt_cheat!!.text.toString()
            if (ed == s + ConstantValue.CHEAT_STATE_1) { //初始炸弹数为10
                ConstantValue.CHEAT_CURRENT_STATE = ConstantValue.CHEAT_STATE_1
                Toast.makeText(this, "Success_炸弹狂魔", Toast.LENGTH_SHORT).show()
                finish()
            } else if (ed == s + ConstantValue.CHEAT_STATE_2) { //子弹补给时间缩短10s
                ConstantValue.CHEAT_CURRENT_STATE = ConstantValue.CHEAT_STATE_2
                Toast.makeText(this, "Success_蓝色风暴", Toast.LENGTH_SHORT).show()
                finish()
            } else if (ed == s + ConstantValue.CHEAT_STATE_3) { //敌机分数每个加100
                ConstantValue.CHEAT_CURRENT_STATE = ConstantValue.CHEAT_STATE_3
                Toast.makeText(this, "Success_得分高手", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
        return super.onKeyDown(keyCode, event)
    }
}