package com.linkprod.shape;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class MainActivity extends Activity implements View.OnClickListener {
    ImageButton play;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        play = (ImageButton) findViewById(R.id.BTN_play);
        play.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        switch(v.getId()) {
            case R.id.BTN_play:
                Intent startGame = new Intent(this, Level.class);
                this.startActivity(startGame);
                break;
        }
    }
}
