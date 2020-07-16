package com.jqorz.planewar.frame;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.jqorz.planewar.R;
import com.jqorz.planewar.constant.PlaneType;
import com.jqorz.planewar.entity.GameView;
import com.jqorz.planewar.listener.GameListener;
import com.jqorz.planewar.tools.TimeTools;
import com.jqorz.planewar.tools.UserDataManager;
import com.jqorz.planewar.constant.ConstantValue;

import java.util.HashMap;

public class GamePlaying extends Activity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener, GameListener {
    private boolean isPause = false;//是否暂停
    private int score = 0;//玩家本局分数
    private int bombNum = 0;//玩家炸弹数量

    private SoundPool soundPool;//声音
    private HashMap<Integer, Integer> soundPoolMap;

    private TextView tv_BombNum, tv_Score;
    private Switch swt_Sound, swt_Music;
    private ImageView imgBtn_Pause;
    private LinearLayout lv_Bomb;
    private GameView gameView;
    private LinearLayout lv_switchers;

    private MediaPlayer mMediaPlayer;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_playing);
        initView();
        initEvent();
        initMusic();
        initSounds();
        setTypeface();
        setSwitch();
        checkMusic();
        checkCheat();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        soundPool.release();
    }

    private void initMusic() {
        mMediaPlayer = MediaPlayer.create(this, R.raw.bgm);
        mMediaPlayer.setLooping(true);
    }


    private void setSwitch() {
        swt_Music.setChecked(UserDataManager.isOpenMusic());
        swt_Sound.setChecked(UserDataManager.isOpenSound());
    }

    private void setTypeface() {
        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/font.ttf");
        tv_BombNum.setTypeface(tf);
        tv_Score.setTypeface(tf);

    }

    private void initView() {
        lv_switchers = findViewById(R.id.lv_switchers);
        gameView = findViewById(R.id.mSurfaceView);
        tv_BombNum = findViewById(R.id.tv_BombNum);
        tv_Score = findViewById(R.id.tv_Score);
        swt_Sound = findViewById(R.id.swt_Sound);
        swt_Music = findViewById(R.id.swt_Music);
        imgBtn_Pause = findViewById(R.id.iv_Pause);
        lv_Bomb = findViewById(R.id.lv_Bomb);
    }

    private void initEvent() {
        gameView.setGameListener(this);
        imgBtn_Pause.setOnClickListener(this);
        swt_Sound.setOnCheckedChangeListener(this);
        swt_Music.setOnCheckedChangeListener(this);
        lv_Bomb.setOnClickListener(this);
    }

    public void initFailView() {//跳转游戏失败界面
        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.stop();
        }
        this.finish();
        Intent intent = new Intent(this, GameOver.class);
        intent.putExtra("Score", score);
        this.startActivity(intent);
    }

    private void initSounds() {
        soundPool = new SoundPool.Builder()
                .setAudioAttributes(new AudioAttributes.Builder().setLegacyStreamType(AudioManager.STREAM_MUSIC).build())
                .setMaxStreams(4)
                .build();
        soundPoolMap = new HashMap<>();
        soundPoolMap.put(2, soundPool.load(this, R.raw.attack, 1));
        soundPoolMap.put(3, soundPool.load(this, R.raw.boom_noraml, 1));
        soundPoolMap.put(4, soundPool.load(this, R.raw.boom_big, 1));
    }

    public void playSound(int sound, int loop) {
        if (UserDataManager.isOpenSound()) {
            AudioManager mgr = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
            float streamVolumeCurrent = mgr.getStreamVolume(AudioManager.STREAM_MUSIC);
            float streamVolumeMax = mgr.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
            float volume = streamVolumeCurrent / streamVolumeMax;
            if (soundPoolMap.get(sound) != null) {
                soundPool.play(soundPoolMap.get(sound), volume, volume, 1, loop, 1f);
            }
        }
    }

    public void pauseThread() {
        if (gameView.mMainRunThread != null) {
            gameView.mMainRunThread.isPlaying = false;
        }
    }

    private void resumeThread() {
        if (gameView.mMainRunThread != null) {
            gameView.mMainRunThread.isPlaying = true;
        }
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.iv_Pause) {
            if (!isPause) {
                setPause();
            } else {
                isPause = false;
                imgBtn_Pause.setBackground(getResources().getDrawable(R.drawable.game_pause_nor));
                TimeTools.setResumeTime();
                checkMusic();
                lv_switchers.setVisibility(View.GONE);
                resumeThread();
            }
        } else if (view.getId() == R.id.lv_Bomb) {
            if (bombNum > 0 && !isPause) {
                bombNum--;
                String showText = "X" + bombNum;
                tv_BombNum.setText(showText);
                gameView.setBomb();
            }
        }
    }

    private void setPause() {
        isPause = true;
        imgBtn_Pause.setBackground(getResources().getDrawable(R.drawable.game_resume_nor));
        TimeTools.setPauseTime();
        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.pause();
        }
        lv_switchers.setVisibility(View.VISIBLE);
        pauseThread();
    }


    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        switch (compoundButton.getId()) {
            case R.id.swt_Sound:
                UserDataManager.setOpenSound(b);
                break;
            case R.id.swt_Music:
                UserDataManager.setOpenMusic(b);
                checkMusic();
                break;
        }
    }

    private void checkMusic() {
        boolean b = UserDataManager.isOpenMusic();
        if (b && !isPause) {
            if (!mMediaPlayer.isPlaying()) {
                mMediaPlayer.start();
            }
        } else {
            if (mMediaPlayer.isPlaying()) {
                mMediaPlayer.pause();
            }
        }
    }

    private void checkCheat() {
        switch (ConstantValue.CHEAT_CURRENT_STATE) {
            case 0://正常状态,还原所有的改动
                bombNum = 0;
                String showText1 = "X" + bombNum;
                tv_BombNum.setText(showText1);
                ConstantValue.SUPPLY_BULLET_INTERVAL_TIME = 25000;
                ConstantValue.ENEMY_TYPE1_SCORE = 100;
                ConstantValue.ENEMY_TYPE2_SCORE = 300;
                ConstantValue.ENEMY_TYPE3_SCORE = 600;
                break;
            case ConstantValue.CHEAT_STATE_1://初始炸弹数为10
                bombNum = 10;
                String showText2 = "X" + bombNum;
                tv_BombNum.setText(showText2);
                break;
            case ConstantValue.CHEAT_STATE_2:
                ConstantValue.SUPPLY_BULLET_INTERVAL_TIME = 17000;//子弹补给间隔时间缩短
                break;
            case ConstantValue.CHEAT_STATE_3://子弹补给间隔时间缩短，敌机分数增加100
                ConstantValue.SUPPLY_BULLET_INTERVAL_TIME = 20000;
                ConstantValue.ENEMY_TYPE1_SCORE = 200;
                ConstantValue.ENEMY_TYPE2_SCORE = 400;
                ConstantValue.ENEMY_TYPE3_SCORE = 700;
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (!isPause) {
            setPause();
        } else {
            isPause = false;
            imgBtn_Pause.setBackground(getResources().getDrawable(R.drawable.game_pause_nor));
            TimeTools.setResumeTime();
            checkMusic();
            lv_switchers.setVisibility(View.GONE);
            resumeThread();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        mMediaPlayer.release();
        this.finish();
    }


    @Override
    public void onGetBomb(int count) {
        bombNum += count;
        runOnUiThread(() -> {
            String showText2 = "X" + bombNum;
            tv_BombNum.setText(showText2);
        });

    }

    @Override
    public void onGameOver() {
        initFailView();
    }

    @Override
    public void onHeroAttacked() {
        playSound(2, 0);
    }

    @Override
    public void onHeroDie() {
        playSound(4, 0);
    }

    @Override
    public void onEnemyDie(@PlaneType int type) {
        this.score += ConstantValue.getEnemyScore(type);
        runOnUiThread(() -> {
            tv_Score.setText(String.valueOf(this.score));
        });
        switch (type) {
            case PlaneType.ENEMY_TYPE1:
                playSound(2, 0);
                break;
            case PlaneType.ENEMY_TYPE2:
                playSound(3, 0);
                break;
            case PlaneType.ENEMY_TYPE3:
                playSound(4, 0);
                break;
        }
    }

    @Override
    public void onEnemyAttacked(int type) {
        if (type != PlaneType.ENEMY_TYPE1) {
            playSound(2, 0);
        }
    }
}