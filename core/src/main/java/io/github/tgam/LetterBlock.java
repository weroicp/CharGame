package io.github.tgam;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.math.Rectangle;

public class LetterBlock {
    private char letter;
    private float x, y, speed;
    private boolean active;
    private BitmapFont font; // 字体对象

    public LetterBlock(char letter, float startX, float startY, float speed, BitmapFont font) {
        this.letter = letter;
        this.x = startX;
        this.y = startY;
        this.speed = speed;
        this.active = true;
        this.font = font; // 初始化字体
    }

    // 更新字母块的位置
    public void update(float deltaTime) {
        if (active) {
            y -= speed * deltaTime;
        }
    }

    // 获取字母
    public char getLetter() {
        return letter;
    }

    // 设置或获取字母块是否激活
    public boolean isActive() {
        return active;
    }

    public void deactivate() {
        this.active = false;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    // 获取字母块的宽度（基于当前全局字体）
    public static float calculateWidth(BitmapFont font, char letter) {
        GlyphLayout layout = new GlyphLayout();
        layout.setText(font, String.valueOf(letter));
        return layout.width;
    }

    // 获取字母块的高度（基于当前全局字体）
    public static float calculateHeight(BitmapFont font) {
        return font.getCapHeight(); // 或者使用 font.getLineHeight()
    }

    public float getWidth() {
        return calculateWidth(font, letter);
    }

    public float getHeight() {
        return calculateHeight(font);
    }

    // 获取字母块的 X 坐标
    public float getX() {
        return x;
    }

    // 获取字母块的 Y 坐标
    public float getY() {
        return y;
    }

    // 获取表示该字母块的矩形边界，用于碰撞检测等
    public Rectangle getBoundingRectangle() {
        return new Rectangle(x, y, getWidth(), getHeight());
    }

    // 返回字母的字符串形式，便于绘制
    public String getText() {
        return String.valueOf(letter);
    }
}
