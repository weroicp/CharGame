package io.github.tgam;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;

public class Word {
    private String text;
    private float x, y, speed;
    private boolean active;

    public Word(String text, float startX, float startY, float speed) {
        this.text = text;
        this.x = startX;
        this.y = startY;
        this.speed = speed;
        this.active = true;
    }

    // 更新单词位置
    public void update(float deltaTime) {
        if (active) {
            y -= speed * deltaTime;
        }
    }

    // 获取和设置方法
    public String getText() { return text; }
    public float getX() { return x; }
    public float getY() { return y; }
    public float getWidth(BitmapFont font) {
        GlyphLayout layout = new GlyphLayout();
        layout.setText(Main.globalFont, text);
        return layout.width;
    }
    public float getHeight() { return 30; } // 高度估计
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}
