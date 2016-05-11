package com.linkprod.shape;

/* Created by Mathis on 26/03/2016. */

import android.os.SystemClock;
import android.provider.Settings;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class Shape{
    public static final int ShpIMG[] = new int[] {
            R.drawable.circle,
            R.drawable.rectangle
    };
    public static int shapesInUse;

    private long PrevPosX[] = new long[3], PrevPosY[] = new long[3], shiftPosX, shiftPosY, speedX, speedY;
    private int altern, shape;
    private float rotation, angle;
    private boolean beingTouched, inUse;
    private RelativeLayout.LayoutParams Pos;
    private ImageView IMG;

    public Shape()
    {
        this.Pos = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        this.inUse = false;
        this.Pos.setMargins(-100, -100, -100, -100);
    }

    public void createShape(int shape, int x, int y, int speedX, int speedY, float angle) {
        this.shape = shape;
        this.Pos.setMargins(x, y, -100, -100);
        this.IMG.setLayoutParams(Pos);
        this.speedX = speedX;
        this.speedY = speedY;
        this.inUse = true;
        this.angle = angle;
        this.IMG.setImageResource(this.ShpIMG[shape]);
        shapesInUse++;
    }

    public void deleteShape() {
        this.inUse = false;
        this.IMG.setImageResource(0);
        shapesInUse--;
    }

    public void setShiftPosX(long value) {
        this.shiftPosX = value;
    }
    public void setShiftPosY(long value) {
        this.shiftPosY = value;
    }
    public void setSpeedX(long value) {
        this.speedX = value;
    }
    public void setSpeedY(long value) {
        this.speedY = value;
    }
    public void setPrevPosX(long value, int pos) {
        this.PrevPosX[pos] = value;
    }
    public void setPrevPosY(long value, int pos) {
        this.PrevPosY[pos] = value;
    }
    public void setAltern(int value) {
        this.altern = value;
    }
    public void setIMG(ImageView value) {
        this.IMG = value;
    }
    public void setBeingTouched(boolean value) {
        this.beingTouched = value;
    }
    public void setRotation(float value) {
        this.rotation = value;
    }
    public long getShiftPosX() {
        return this.shiftPosX;
    }
    public long getShiftPosY() {
        return this.shiftPosY;
    }
    public long getSpeedX() {
        return this.speedX;
    }
    public long getSpeedY() {
        return this.speedY;
    }
    public long getPrevPosX(int pos) {
        return this.PrevPosX[pos];
    }
    public long getPrevPosY(int pos) {
        return this.PrevPosY[pos];
    }
    public int getAltern() {
        return this.altern;
    }
    public RelativeLayout.LayoutParams getPos() {
        return this.Pos;
    }
    public ImageView getIMG() {
        return this.IMG;
    }
    public boolean getBeingTouched() {
        return this.beingTouched;
    }
    public int getShape() {
        return this.shape;
    }
    public boolean getInUse() {
        return this.inUse;
    }
    public float getRotation() {
        return this.rotation;
    }
    public float getAngle() {
        return this.angle;
    }
}
