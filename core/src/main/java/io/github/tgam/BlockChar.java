package io.github.tgam;

import com.badlogic.gdx.math.Rectangle;

import java.lang.reflect.Array;
import java.util.List;

public class BlockChar {
    private float x;
    private float y;// 包含位置和尺寸信息
    private char displayChar;

    public BlockChar(float x, float y, char displayChar) {
        this.x = x;
        this.y = y;
        this.displayChar = displayChar;
    }

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public char getDisplayChar() {
        return displayChar;
    }

    public void setDisplayChar(char displayChar) {
        this.displayChar = displayChar;
    }

    public float getX(){
        return this.x;
    }

    public float getY(){
        return this.y;
    }

    public void updateY(float deltay){
        this.y+=deltay;
    }
}
