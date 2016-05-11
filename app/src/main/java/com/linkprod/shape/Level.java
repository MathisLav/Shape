package com.linkprod.shape;

// Created by Mathis on 28/03/2016.

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.view.Display;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class Level extends Activity implements OnTouchListener {
    Shape shpObj[] = new Shape[] {new Shape(), new Shape(), new Shape(), new Shape(), new Shape(), new Shape(), new Shape(), new Shape()};
    ImageView IMG[] = new ImageView[8], IMG_temp;
    RelativeLayout.LayoutParams LAY_temp;
    SharedPreferences highScore;
    SharedPreferences.Editor editor;
    TextView score;
    Typeface caviarDreams;
    double timeBefNextThrow;
    int width, height, left, right, top, points;
    byte lives, nbrPerThrow;
    final byte CIRC = 0, RECT = 1, CROS = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level);

        highScore = PreferenceManager.getDefaultSharedPreferences(this);
        editor = highScore.edit();

        score = (TextView) findViewById(R.id.score);
        caviarDreams = Typeface.createFromAsset(getAssets(), "fonts/CaviarDreams.ttf");
        score.setTypeface(caviarDreams);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        width = size.x;
        height = size.y;

        // setting the location of the "shadows"

        IMG_temp = (ImageView) findViewById(R.id.CIRC_shadow);
        LAY_temp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        LAY_temp.setMargins(-55, height/2-100, 0, 0);
        IMG_temp.setLayoutParams(LAY_temp);
        AlphaAnimation alpha = new AlphaAnimation(1f, 0.3f);
        alpha.setDuration(0);
        alpha.setFillAfter(true);
        IMG_temp.startAnimation(alpha);
        IMG_temp = (ImageView) findViewById(R.id.RECT_shadow);
        LAY_temp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        LAY_temp.setMargins(width-95, height/2-110, -60, 0);
        IMG_temp.setLayoutParams(LAY_temp);
        alpha = new AlphaAnimation(1f, 0.3f);
        alpha.setDuration(0);
        alpha.setFillAfter(true);
        IMG_temp.startAnimation(alpha);

        //

        Shape.shapesInUse = 0;
        IMG = new ImageView[] {(ImageView) findViewById(R.id.IMG_1), (ImageView) findViewById(R.id.IMG_2), (ImageView) findViewById(R.id.IMG_3), (ImageView) findViewById(R.id.IMG_4), (ImageView) findViewById(R.id.IMG_5), (ImageView) findViewById(R.id.IMG_6), (ImageView) findViewById(R.id.IMG_7), (ImageView) findViewById(R.id.IMG_8)};
        for(int i=0; i<8; i++) {
            shpObj[i].setIMG(IMG[i]);
            shpObj[i].getIMG().setOnTouchListener(this);
        }

        LevelStart();
    }


    public void LevelStart() {
        score.setText("0");
        left = CIRC;
        right = RECT;
        top = CROS;
        lives = 2;
        nbrPerThrow = 3;

        final Handler handlerShapes = new Handler();
        handlerShapes.postDelayed(new Runnable() {
            @Override
            public void run() {
                /*    ### On s'occupe des Shapes ###     */
                for (int i = 0; i < 8; i++) {
                    if (shpObj[i].getInUse() && !shpObj[i].getBeingTouched()) {
                        shpObj[i].getPos().setMargins(shpObj[i].getPos().leftMargin + (int) shpObj[i].getSpeedX(), shpObj[i].getPos().topMargin + (int) shpObj[i].getSpeedY(), -100, -100);
                        shpObj[i].getIMG().setLayoutParams(shpObj[i].getPos());
                        shpObj[i].setSpeedY((int)shpObj[i].getSpeedY() + 1);

                        shpObj[i].setRotation(shpObj[i].getRotation() - shpObj[i].getAngle()/2);
                        shpObj[i].getIMG().setRotation(shpObj[i].getRotation());
                        if (shpObj[i].getPos().leftMargin < -150) {
                            shpObj[i].deleteShape();
                            if (shpObj[i].getShape() == left)
                                onGoal();
                            else
                                lifeLost();
                        } else if (shpObj[i].getPos().leftMargin > width) {
                            shpObj[i].deleteShape();
                            if (shpObj[i].getShape() == right)
                                onGoal();
                            else
                                lifeLost();
                        } else if (shpObj[i].getPos().topMargin > height) {
                            shpObj[i].deleteShape();
                            if (shpObj[i].getShape() != top)
                                lifeLost();
                        }
                    }
                }
                /*    ### On s'occupe des vagues ###     */
                if(System.currentTimeMillis() >= timeBefNextThrow) {
                    int numberNewShapes = 0;
                    while(numberNewShapes <= 0)
                        numberNewShapes = (int)(Math.random()*3+nbrPerThrow-3);
                    for(int i=0; i<numberNewShapes; i++) {
                        int j = 0;
                        while(shpObj[j].getInUse())
                            j++;
                        int pos_temp = (int)(Math.random()*(width/3));
                        int speedY_temp = (int)(Math.random()*-10-28);
                        int speedX_temp = (int)(Math.random()*-((width-pos_temp)/(2*speedY_temp)+1)+1);
                        float angle = -1;
                        if(Math.random()*2 < 1) {
                            pos_temp = width - pos_temp - 120;
                            speedX_temp = -speedX_temp;
                            angle = 1;
                        }
                        shpObj[j].createShape((int)(Math.random()*2), pos_temp, height, speedX_temp, speedY_temp, angle);
                    }
                    timeBefNextThrow = System.currentTimeMillis() + Math.random()*1500+1000;
                }

                /*    ### On s'occupe du temps ###     */
                if(lives >= 0)
                    handlerShapes.postDelayed(this, 1000/50);
                else {
                    if(highScore.getInt("HIGH", 0) < points) {
                        editor.putInt("HIGH", points);
                        editor.commit();
                    }
                    end();
                }
            }
        }, 1000/50);
    }

    public void lifeLost() {
        lives -= 1;
        if(lives == 1)
            IMG_temp = (ImageView) findViewById(R.id.IMG_live2);
        else
            IMG_temp = (ImageView) findViewById(R.id.IMG_live1);

        if(lives != -1) {
            AlphaAnimation alpha = new AlphaAnimation(1f, 0.5f);
            alpha.setDuration(250);
            alpha.setFillAfter(true);
            IMG_temp.startAnimation(alpha);
        }

        Vibrator v = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        v.vibrate(150);
    }

    public void onGoal() {
        points += 10;
        if(points % 100 == 0)
            nbrPerThrow += 1;

        score.setText("" + points);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event)
    {
        ImageView imgOnTouch = (ImageView) findViewById(v.getId());
        int i = 0;
        switch(v.getId()) {
            case R.id.IMG_1: i=0; break;
            case R.id.IMG_2: i=1; break;
            case R.id.IMG_3: i=2; break;
            case R.id.IMG_4: i=3; break;
            case R.id.IMG_5: i=4; break;
            case R.id.IMG_6: i=5; break;
            case R.id.IMG_7: i=6; break;
            case R.id.IMG_8: i=7; break;
        }
        if(event.getAction() == MotionEvent.ACTION_DOWN) {
            shpObj[i].setBeingTouched(true);
            shpObj[i].setShiftPosX((int) event.getRawX() - shpObj[i].getPos().leftMargin);
            shpObj[i].setShiftPosY((int) event.getRawY() - shpObj[i].getPos().topMargin);
        }
        if(event.getAction() == MotionEvent.ACTION_UP) {
            if(shpObj[i].getAltern() == 2)
                shpObj[i].setAltern(-1);
            shpObj[i].setSpeedX((int)((event.getRawX() - shpObj[i].getPrevPosX(shpObj[i].getAltern()+1))/1.8));
            shpObj[i].setSpeedY((int)((event.getRawY() - shpObj[i].getPrevPosY(shpObj[i].getAltern()+1))/1.8));
            shpObj[i].setBeingTouched(false);
        }
        shpObj[i].getPos().setMargins((int) ((long) event.getRawX() - shpObj[i].getShiftPosX()), (int) ((long) event.getRawY() - shpObj[i].getShiftPosY()), -100, -100);
        imgOnTouch.setLayoutParams(shpObj[i].getPos());

        shpObj[i].setAltern(shpObj[i].getAltern()+1);
        if(shpObj[i].getAltern() == 3)
            shpObj[i].setAltern(0);
        shpObj[i].setPrevPosX((int)event.getRawX(), shpObj[i].getAltern());
        shpObj[i].setPrevPosY((int)event.getRawY(), shpObj[i].getAltern());

        return true;
    }


    public void end() {
        ImageView IMG_sdwC = (ImageView) findViewById(R.id.CIRC_shadow);
        ImageView IMG_sdwR = (ImageView) findViewById(R.id.RECT_shadow);

        int originalPos[] = new int[2];
        IMG_sdwC.getLocationOnScreen(originalPos);
        TranslateAnimation anim_temp = new TranslateAnimation(0, width/2-150 - originalPos[0], 0, 0);

        anim_temp.setDuration(750);
        anim_temp.setFillAfter(true);
        AlphaAnimation alpha = new AlphaAnimation(0.3f, 0.7f);
        alpha.setDuration(750);
        alpha.setFillAfter(true);
        IMG_sdwC.startAnimation(alpha);
        IMG_sdwC.startAnimation(anim_temp);

        IMG_sdwR.getLocationOnScreen(originalPos);
        anim_temp = new TranslateAnimation(0, width/2 - originalPos[0], 0, 0);
        anim_temp.setDuration(750);
        anim_temp.setFillAfter(true);
        IMG_sdwR.startAnimation(alpha);
        IMG_sdwR.startAnimation(anim_temp);
        //finish();
    }

}