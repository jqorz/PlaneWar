package com.jqorz.planewar.frame;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.jqorz.planewar.R;

public class GameLauncher extends Activity implements View.OnClickListener,View.OnLongClickListener {
    private ImageButton imgBtn_startGame, imgBtn_exitGame, imgBtn_about, imgBtn_setting;
private ImageView iv_icon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_launcher);
        initView();
        initEvent();
    }

    private void initView() {
        imgBtn_startGame = (ImageButton) findViewById(R.id.imgBtn_startGame);
        imgBtn_exitGame = (ImageButton) findViewById(R.id.imgBtn_exitGame);
        imgBtn_about = (ImageButton) findViewById(R.id.imgBtn_about);
        imgBtn_setting = (ImageButton) findViewById(R.id.imgBtn_Setting);
        iv_icon= (ImageView) findViewById(R.id.iv_icon);
    }

    private void initEvent() {
        imgBtn_startGame.setOnClickListener(this);
        imgBtn_exitGame.setOnClickListener(this);
        imgBtn_about.setOnClickListener(this);
        imgBtn_setting.setOnClickListener(this);
        iv_icon.setOnLongClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgBtn_startGame:
                Intent intent1 = new Intent(this, GamePlaying.class);
                this.startActivity(intent1);
                this.finish();
                break;
            case R.id.imgBtn_about:
                Intent intent2 = new Intent(this, GameAbout.class);
                this.startActivity(intent2);
                break;
            case R.id.imgBtn_Setting:
                Intent intent3 = new Intent(this, GameSetting.class);
                this.startActivity(intent3);
                break;
            case R.id.imgBtn_exitGame:
                this.finish();
                break;
        }
    }


    @Override
    public boolean onLongClick(View view) {
        if (view.getId()==R.id.iv_icon){
            Intent intent = new Intent(this, GameCheat.class);
            this.startActivity(intent);
        }
        return false;
    }
}
