package com.jqorz.planewar.frame;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.Toast;

import com.jqorz.planewar.R;
import com.jqorz.planewar.constant.ConstantValue;

public class GameCheat extends Activity {
    private EditText edtTxt_cheat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_cheat);
        initView();
    }

    private void initView() {
        edtTxt_cheat = (EditText) findViewById(R.id.edtTxt_cheat);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            String s = "jqorz_";
            String ed = edtTxt_cheat.getText().toString();
            if (ed.equals(s + ConstantValue.CHEAT_STATE_1)) {//初始炸弹数为10
                ConstantValue.CHEAT_CURRENT_STATE = ConstantValue.CHEAT_STATE_1;
                Toast.makeText(this,"Success_炸弹狂魔",Toast.LENGTH_SHORT).show();
                this.finish();
            }
            else if (ed.equals(s + ConstantValue.CHEAT_STATE_2)) {//子弹补给时间缩短10s
                ConstantValue.CHEAT_CURRENT_STATE = ConstantValue.CHEAT_STATE_2;
                Toast.makeText(this,"Success_蓝色风暴",Toast.LENGTH_SHORT).show();
                this.finish();
            }
            else if (ed.equals(s + ConstantValue.CHEAT_STATE_3)) {//敌机分数每个加100
                ConstantValue.CHEAT_CURRENT_STATE = ConstantValue.CHEAT_STATE_3;
                Toast.makeText(this,"Success_得分高手",Toast.LENGTH_SHORT).show();
                this.finish();
            }

        }
        return super.onKeyDown(keyCode, event);
    }
}