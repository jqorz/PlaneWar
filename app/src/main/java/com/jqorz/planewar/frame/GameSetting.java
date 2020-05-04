package com.jqorz.planewar.frame;

import android.app.Activity;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.jqorz.planewar.R;
import com.jqorz.planewar.tools.UserDataManager;

public class GameSetting extends Activity implements CompoundButton.OnCheckedChangeListener {
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
        swt_Music.setChecked(UserDataManager.isOpenMusic());
        swt_Sound.setChecked(UserDataManager.isOpenSound());
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
        switch (compoundButton.getId()) {
            case R.id.swt_Sound:
                UserDataManager.setOpenSound(b);
                break;
            case R.id.swt_Music:
                UserDataManager.setOpenMusic(b);
                break;
        }
    }

}
