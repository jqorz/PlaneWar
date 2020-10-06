package com.jqorz.planewar.frame

import android.app.Activity
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.jqorz.planewar.R
import com.jqorz.planewar.constant.ConstantValue
import com.jqorz.planewar.utils.UserDataUtil

class GameOver : Activity(), View.OnClickListener {
    private val MAX_SCORE = "MAX_SCORE"
    private val HAS_CHEATED = "HAS_CHEATED"
    private var tv_MaxScore: TextView? = null
    private var tv_thisScore: TextView? = null
    private var imgBtn_restartGame: ImageView? = null
    private var imgBtn_backHome: ImageView? = null
    private var imgBtn_exitGame: ImageView? = null
    override fun onCreate(savedInstanceState: Bundle) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.game_over)
        initView()
        initEvent()
        setTypeface()
        bundle
        ConstantValue.CHEAT_CURRENT_STATE = 0 //还原作弊状态
    }//如果最高分作弊，用"."标识出来//如果此次作弊，用"."标识出来

    //如果作弊得到最高分，保存作弊记录至本地
    private val bundle: Unit
        private get() {
            val intent = intent
            val thisScore = intent.getIntExtra("Score", 0)
            val isCheated = ConstantValue.CHEAT_CURRENT_STATE != 0
            var maxScore = UserDataUtil.loadUserIntegerData(this, MAX_SCORE)
            if (thisScore > maxScore) {
                maxScore = thisScore
                UserDataUtil.updateUserData(this, MAX_SCORE, maxScore)
                UserDataUtil.updateUserData(this, HAS_CHEATED, isCheated) //如果作弊得到最高分，保存作弊记录至本地
            }
            val maxScoreCheated = UserDataUtil.loadUserBooleanData(this, HAS_CHEATED)
            var showText1 = "" + thisScore
            var showText2 = "" + maxScore
            if (isCheated) { //如果此次作弊，用"."标识出来
                showText1 = "$showText1."
            }
            if (maxScoreCheated) { //如果最高分作弊，用"."标识出来
                showText2 = "$showText2."
            }
            tv_thisScore!!.text = showText1
            tv_MaxScore!!.text = showText2
        }

    private fun initEvent() {
        imgBtn_restartGame!!.setOnClickListener(this)
        imgBtn_backHome!!.setOnClickListener(this)
        imgBtn_exitGame!!.setOnClickListener(this)
    }

    private fun setTypeface() {
        val tf = Typeface.createFromAsset(assets, "fonts/font.ttf") //根据路径得到Typeface
        tv_MaxScore!!.typeface = tf //设置字体
        tv_thisScore!!.typeface = tf //设置字体
    }

    private fun initView() {
        tv_MaxScore = findViewById<View>(R.id.tv_MaxScore) as TextView
        tv_thisScore = findViewById<View>(R.id.tv_thisScore) as TextView
        imgBtn_restartGame = findViewById<View>(R.id.imgBtn_restartGame) as ImageView
        imgBtn_backHome = findViewById<View>(R.id.imgBtn_backHome) as ImageView
        imgBtn_exitGame = findViewById<View>(R.id.imgBtn_exitGame) as ImageView
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.imgBtn_restartGame -> {
                val intent1 = Intent(this, GamePlaying::class.java)
                this.startActivity(intent1)
                finish()
            }
            R.id.imgBtn_backHome -> {
                val intent2 = Intent(this, GameLauncher::class.java)
                this.startActivity(intent2)
                finish()
            }
            R.id.imgBtn_exitGame -> finish()
        }
    }
}