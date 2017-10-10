package com.jqorz.planewar.frame;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.jqorz.planewar.R;

public class GameSetting extends Activity implements CompoundButton.OnCheckedChangeListener {
    SharedPreferences sp;
    private Switch swt_Sound, swt_Music;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_setting);
        initView();
        initEvent();
        setSwitch();
    }


    private void setSwitch() {
        sp = getSharedPreferences("Data", MODE_PRIVATE);
        Boolean b1 = sp.getBoolean("swt_Music", true);
        swt_Music.setChecked(b1);
        Boolean b2 = sp.getBoolean("swt_Sound", true);
        swt_Sound.setChecked(b2);
    }

    private void initView() {
        swt_Sound = (Switch) findViewById(R.id.swt_Sound);
        swt_Music = (Switch) findViewById(R.id.swt_Music);

    }

    private void initEvent() {
        swt_Sound.setOnCheckedChangeListener(this);
        swt_Music.setOnCheckedChangeListener(this);
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
    }

}
