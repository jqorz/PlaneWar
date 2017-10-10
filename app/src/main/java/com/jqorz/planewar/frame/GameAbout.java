package com.jqorz.planewar.frame;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.jqorz.planewar.R;

public class GameAbout extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_about);
    }

    public void mClick(View v) {
        this.finish();
    }


}
