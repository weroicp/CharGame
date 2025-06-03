package io.github.tgam;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Block {
    public final float x, y;
    public boolean isActive;

    public Block(float x, float y) {
        this.x = x;
        this.y = y;
        this.isActive = true;
    }

    public void draw(SpriteBatch spriteBatch, Texture texture) {
        if (isActive) {
            spriteBatch.draw(texture, x, y, Main.BLOCK_WIDTH, Main.BLOCK_HEIGHT);
        }
    }
}
