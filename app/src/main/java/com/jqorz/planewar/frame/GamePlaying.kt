package com.jqorz.planewar.frame

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.SoundPool
import android.os.Bundle
import android.view.View
import android.widget.CompoundButton
import com.jqorz.planewar.R
import com.jqorz.planewar.constant.ConstantValue
import com.jqorz.planewar.constant.PlaneType
import com.jqorz.planewar.listener.GameListener
import com.jqorz.planewar.tools.TimeTools
import com.jqorz.planewar.tools.UserDataManager
import kotlinx.android.synthetic.main.game_playing.*
import java.util.*

class GamePlaying : Activity(), View.OnClickListener, CompoundButton.OnCheckedChangeListener, GameListener {
    private var isPause = false //是否暂停
    private var score = 0 //玩家本局分数
    private var bombNum = 0 //玩家炸弹数量
    private lateinit var soundPool: SoundPool //声音
    private lateinit var soundPoolMap: HashMap<Int, Int>
    private lateinit var mMediaPlayer: MediaPlayer

    public override fun onCreate(savedInstanceState: Bundle) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.game_playing)
        initEvent()
        initMusic()
        initSounds()
        setTypeface()
        setSwitch()
        checkMusic()
        checkCheat()
    }

    override fun onDestroy() {
        super.onDestroy()
        soundPool.release()
    }

    private fun initMusic() {
        mMediaPlayer = MediaPlayer.create(this, R.raw.bgm)
        mMediaPlayer.isLooping = true
    }

    private fun setSwitch() {
        swt_Music!!.isChecked = UserDataManager.isOpenMusic()
        swt_Sound!!.isChecked = UserDataManager.isOpenSound()
    }

    private fun setTypeface() {
        val tf = Typeface.createFromAsset(assets, "fonts/font.ttf")
        tv_BombNum!!.typeface = tf
        tv_Score!!.typeface = tf
    }


    private fun initEvent() {
        gameView!!.setGameListener(this)
        imgBtn_Pause!!.setOnClickListener(this)
        swt_Sound!!.setOnCheckedChangeListener(this)
        swt_Music!!.setOnCheckedChangeListener(this)
        lv_Bomb!!.setOnClickListener(this)
    }

    private fun toGameOverActivity() { //跳转游戏失败界面
        if (mMediaPlayer.isPlaying) {
            mMediaPlayer.stop()
        }
        finish()
        val intent = Intent(this, GameOver::class.java)
        intent.putExtra("Score", score)
        this.startActivity(intent)
    }

    private fun initSounds() {
        soundPool = SoundPool.Builder()
                .setAudioAttributes(AudioAttributes.Builder().setLegacyStreamType(AudioManager.STREAM_MUSIC).build())
                .setMaxStreams(4)
                .build()
        soundPoolMap = HashMap()
        soundPoolMap[2] = soundPool.load(this, R.raw.attack, 1)
        soundPoolMap[3] = soundPool.load(this, R.raw.boom_noraml, 1)
        soundPoolMap[4] = soundPool.load(this, R.raw.boom_big, 1)
    }

    fun playSound(sound: Int, loop: Int) {
        if (UserDataManager.isOpenSound()) {
            val mgr = this.getSystemService(Context.AUDIO_SERVICE) as AudioManager
            val streamVolumeCurrent = mgr.getStreamVolume(AudioManager.STREAM_MUSIC).toFloat()
            val streamVolumeMax = mgr.getStreamMaxVolume(AudioManager.STREAM_MUSIC).toFloat()
            val volume = streamVolumeCurrent / streamVolumeMax
            if (soundPoolMap[sound] != null) {
                soundPool.play(soundPoolMap[sound]!!, volume, volume, 1, loop, 1f)
            }
        }
    }

    fun pauseThread() {
        gameView!!.mMainRunThread.isPlaying = false

    }

    private fun resumeThread() {
        gameView!!.mMainRunThread.isPlaying = true
    }

    override fun onClick(view: View) {
        if (view.id == R.id.imgBtn_Pause) {
            if (!isPause) {
                setPause()
            } else {
                isPause = false
                imgBtn_Pause!!.background = resources.getDrawable(R.drawable.game_pause_nor)
                TimeTools.setResumeTime()
                checkMusic()
                lv_switchers!!.visibility = View.GONE
                resumeThread()
            }
        } else if (view.id == R.id.lv_Bomb) {
            if (bombNum > 0 && !isPause) {
                bombNum--
                val showText = "X$bombNum"
                tv_BombNum!!.text = showText
                gameView!!.setBomb()
            }
        }
    }

    private fun setPause() {
        isPause = true
        imgBtn_Pause!!.background = resources.getDrawable(R.drawable.game_resume_nor)
        TimeTools.setPauseTime()
        if (mMediaPlayer.isPlaying) {
            mMediaPlayer.pause()
        }
        lv_switchers!!.visibility = View.VISIBLE
        pauseThread()
    }

    override fun onCheckedChanged(compoundButton: CompoundButton, b: Boolean) {
        when (compoundButton.id) {
            R.id.swt_Sound -> UserDataManager.setOpenSound(b)
            R.id.swt_Music -> {
                UserDataManager.setOpenMusic(b)
                checkMusic()
            }
        }
    }

    private fun checkMusic() {
        val b = UserDataManager.isOpenMusic()
        if (b && !isPause) {
            if (!mMediaPlayer.isPlaying) {
                mMediaPlayer.start()
            }
        } else {
            if (mMediaPlayer.isPlaying) {
                mMediaPlayer.pause()
            }
        }
    }

    private fun checkCheat() {
        when (ConstantValue.CHEAT_CURRENT_STATE) {
            0 -> {
                bombNum = 0
                val showText1 = "X$bombNum"
                tv_BombNum!!.text = showText1
                ConstantValue.SUPPLY_BULLET_INTERVAL_TIME = 25000
                ConstantValue.ENEMY_TYPE1_SCORE = 100
                ConstantValue.ENEMY_TYPE2_SCORE = 300
                ConstantValue.ENEMY_TYPE3_SCORE = 600
            }
            ConstantValue.CHEAT_STATE_1 -> {
                bombNum = 10
                val showText2 = "X$bombNum"
                tv_BombNum!!.text = showText2
            }
            ConstantValue.CHEAT_STATE_2 -> ConstantValue.SUPPLY_BULLET_INTERVAL_TIME = 17000 //子弹补给间隔时间缩短
            ConstantValue.CHEAT_STATE_3 -> {
                ConstantValue.SUPPLY_BULLET_INTERVAL_TIME = 20000
                ConstantValue.ENEMY_TYPE1_SCORE = 200
                ConstantValue.ENEMY_TYPE2_SCORE = 400
                ConstantValue.ENEMY_TYPE3_SCORE = 700
            }
        }
    }

    override fun onBackPressed() {
        if (!isPause) {
            setPause()
        } else {
            isPause = false
            imgBtn_Pause!!.background = resources.getDrawable(R.drawable.game_pause_nor)
            TimeTools.setResumeTime()
            checkMusic()
            lv_switchers!!.visibility = View.GONE
            resumeThread()
        }
    }

    override fun onStop() {
        super.onStop()
        mMediaPlayer.release()
        finish()
    }

    override fun onGetBomb(count: Int) {
        bombNum += count
        runOnUiThread {
            val showText2 = "X$bombNum"
            tv_BombNum!!.text = showText2
        }
    }

    override fun onGameOver() {
        toGameOverActivity()
    }

    override fun onHeroAttacked() {
        playSound(2, 0)
    }

    override fun onHeroDie() {
        playSound(4, 0)
    }

    override fun onEnemyDie(type: PlaneType) {
        score += ConstantValue.getEnemyScore(type)
        runOnUiThread { tv_Score!!.text = score.toString() }
        when (type) {
            PlaneType.ENEMY_TYPE1 -> playSound(2, 0)
            PlaneType.ENEMY_TYPE2 -> playSound(3, 0)
            PlaneType.ENEMY_TYPE3 -> playSound(4, 0)
        }
    }

    override fun onEnemyAttacked(type: PlaneType) {
        if (type != PlaneType.ENEMY_TYPE1) {
            playSound(2, 0)
        }
    }
}