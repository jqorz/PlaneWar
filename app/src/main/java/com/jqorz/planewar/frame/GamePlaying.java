package com.jqorz.planewar.frame;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.jqorz.planewar.entity.GameView;
import com.jqorz.planewar.R;
import com.jqorz.planewar.utils.ConstantUtil;

public class GamePlaying extends Activity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    GameView gameView;//GameView的引用
    SharedPreferences sp;

    Boolean isPause = false;//是否暂停
    private int score = 0;//玩家本局分数
    private int bombNum = 0;//玩家炸弹数量
    private boolean isSound = true;//是否播放声音

    private long pauseTime = 0L;//记录暂停时间
    private long resumeTime = 0L;//记录恢复时间

    private TextView tv_BombNum, tv_Score;
    private Switch swt_Sound, swt_Music;
    private ImageButton imgBtn_Pause;
    private LinearLayout lv_Bomb;

    private MediaPlayer mMediaPlayer;

    public Handler myHandler = new Handler() {//用来更新UI线程中的控件
        public void handleMessage(Message msg) {
            if (msg.what == ConstantUtil.STATE_END) {//游戏失败，玩家飞机坠毁
                postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (gameView != null) {
                            gameView.moveThread.setFlag(false);
                            gameView.runThread.setFlag(false);
                            gameView.explodeThread.setFlag(false);
                            gameView.thread.setFlag(false);
                            gameView = null;
                        }
                        initFailView();//切换到FailView
                    }
                }, 800);//为了使英雄飞机爆炸的动画能够播放完，以此延迟执行

            } else {
                score = score + msg.arg1;
                String showText = "" + score;
                tv_Score.setText(showText);

                bombNum = bombNum + msg.arg2;
                String showText2 = "X" + bombNum;
                tv_BombNum.setText(showText2);
            }
        }
    };
    private LinearLayout lv_switchers;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_playing);
        sp = getSharedPreferences("Data", MODE_PRIVATE);
        initView();
        initEvent();
        initMusic();
        setTypeface();
        getSetting();
        setSwitch();
        checkMusic();
        checkCheat();
    }


    private void initMusic() {
        mMediaPlayer = MediaPlayer.create(this, R.raw.bgm);
        mMediaPlayer.setLooping(true);
    }

    public boolean getIsSound() {
        getSetting();
        return isSound;
    }


    private void getSetting() {
        isSound = sp.getBoolean("swt_Sound", true);
    }

    private void setSwitch() {
        Boolean b1 = sp.getBoolean("swt_Music", false);
        swt_Music.setChecked(b1);
        Boolean b2 = sp.getBoolean("swt_Sound", true);
        swt_Sound.setChecked(b2);
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
        imgBtn_Pause = findViewById(R.id.imgBtn_Pause);
        lv_Bomb = findViewById(R.id.lv_Bomb);
    }

    private void initEvent() {
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

    public void initPauseView() {
        if (gameView.thread != null) {
            gameView.thread.flag2 = false;
        }
    }

    private void initResumeView() {
        if (gameView.thread != null) {
            gameView.thread.flag2 = true;
        }
    }

    public long getCurrentTime() {
        long now = System.currentTimeMillis();
        if (resumeTime - pauseTime >= 0) {
            return now - (resumeTime - pauseTime);
        } else {
            return now;
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.imgBtn_Pause) {
            if (!isPause) {
                setPause();
            } else {
                isPause = false;
                imgBtn_Pause.setBackground(getResources().getDrawable(R.drawable.game_pause_nor));
                resumeTime = System.currentTimeMillis();
                checkMusic();
                lv_switchers.setVisibility(View.GONE);
                initResumeView();
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
        pauseTime = System.currentTimeMillis();
        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.pause();
        }
        lv_switchers.setVisibility(View.VISIBLE);
        initPauseView();
    }


    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        SharedPreferences.Editor editor = sp.edit();
        switch (compoundButton.getId()) {
            case R.id.swt_Sound:
                editor.putBoolean("swt_Sound", b);
                break;
            case R.id.swt_Music:
                editor.putBoolean("swt_Music", b);
                break;
        }
        editor.apply();
        checkMusic();
    }

    private void checkMusic() {
        boolean b = sp.getBoolean("swt_Music", true);
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
        switch (ConstantUtil.CHEAT_CURRENT_STATE) {
            case 0://正常状态,还原所有的改动
                bombNum = 0;
                String showText1 = "X" + bombNum;
                tv_BombNum.setText(showText1);
                ConstantUtil.SUPPLY_BULLET_INTERVAL_TIME = 25000;
                ConstantUtil.ENEMY_TYPE1_SCORE = 100;
                ConstantUtil.ENEMY_TYPE2_SCORE = 300;
                ConstantUtil.ENEMY_TYPE3_SCORE = 600;
                break;
            case ConstantUtil.CHEAT_STATE_1://初始炸弹数为10
                bombNum = 10;
                String showText2 = "X" + bombNum;
                tv_BombNum.setText(showText2);
                break;
            case ConstantUtil.CHEAT_STATE_2:
                ConstantUtil.SUPPLY_BULLET_INTERVAL_TIME = 17000;//子弹补给间隔时间缩短
                break;
            case ConstantUtil.CHEAT_STATE_3://子弹补给间隔时间缩短，敌机分数增加100
                ConstantUtil.SUPPLY_BULLET_INTERVAL_TIME = 20000;
                ConstantUtil.ENEMY_TYPE1_SCORE = 200;
                ConstantUtil.ENEMY_TYPE2_SCORE = 400;
                ConstantUtil.ENEMY_TYPE3_SCORE = 700;
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
            resumeTime = System.currentTimeMillis();
            checkMusic();
            lv_switchers.setVisibility(View.GONE);
            initResumeView();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        mMediaPlayer.release();
        this.finish();
    }
}