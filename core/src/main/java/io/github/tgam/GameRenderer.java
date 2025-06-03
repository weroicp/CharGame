package io.github.tgam;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.List;

public class GameRenderer {
    private final SpriteBatch spriteBatch;
    private final OrthographicCamera camera;
    private Viewport viewport;
    private Rectangle tmpRect;

    public GameRenderer(OrthographicCamera camera,Viewport viewport) {
        // 初始化摄像机
        this.camera = camera;

        this.viewport=viewport;

        spriteBatch = new SpriteBatch();

        this.tmpRect=new Rectangle();
    }

    public void render(float delta,GameLogicManager gameLogicManager,String inputText) {
        // 设置 SpriteBatch 的投影矩阵为当前相机
        spriteBatch.setProjectionMatrix(camera.combined);
        spriteBatch.begin();

        // 使用全局资源 Main.globalFont 和 Main.WALL_TEXTURE
        drawWalls(gameLogicManager.getBlockGrid());
        drawTextLines(gameLogicManager.getFallingTextLines());
        drawWallTextLines(gameLogicManager.getWallTextLines());
        drawUI(gameLogicManager.getScore(),inputText);

        spriteBatch.end();
    }

    private void drawWalls(BlockGrid blockGrid) {
        for (int i = 0; i < Main.WALL_HEIGHT_IN_BLOCKS; i++) {
            for (int j = 0; j < Main.WALL_WIDTH_IN_BLOCKS; j++) {
                if(!blockGrid.hasBlockAt(i,j))continue;;

                float x = j * Main.BLOCK_WIDTH;
                float y = Main.WALL_BOTTOM + i * Main.BLOCK_HEIGHT;
                spriteBatch.draw(Main.WALL_TEXTURE, x, y, Main.BLOCK_WIDTH, Main.BLOCK_HEIGHT);
            }
        }
    }

    private void drawTextLines(List<TextLine> textLines) {
        for (TextLine line : textLines) {
            for (BlockChar blockChar : line.getCharacters()) {
                char c = blockChar.getDisplayChar();

                Main.globalFont.draw(spriteBatch, String.valueOf(c), blockChar.getX(), blockChar.getY()+Main.BLOCK_HEIGHT);
            }
        }
    }

    private void drawWallTextLines(List<BlockChar> chars){
        for (BlockChar blockChar : chars) {
            char c = blockChar.getDisplayChar();

//            spriteBatch.draw(textureRegion, tmpRect.x, tmpRect.y, tmpRect.width, tmpRect.height);
            Main.globalFont.draw(spriteBatch, String.valueOf(c), blockChar.getX(), blockChar.getY()+Main.BLOCK_HEIGHT);
        }
    }

    private void drawUI(int score, String inputText) {
        Main.globalFont.draw(spriteBatch, "Score: " + score, Main.BLOCK_WIDTH, Main.GAME_HEIGHT-Main.BLOCK_HEIGHT*2);
        Main.globalFont.draw(spriteBatch, "Input: " + inputText, Main.BLOCK_WIDTH, Main.GAME_HEIGHT-Main.BLOCK_HEIGHT);
    }

    public void dispose() {
        spriteBatch.dispose();
    }

    public void resize(int width, int height) {
        // 如果需要更新视口，可以在这里处理
    }

    public void drawGameOverScreen(int score) {
        spriteBatch.begin();
        Main.globalFont.getData().setScale(2);
        Main.globalFont.draw(spriteBatch, "Game Over!", 300, 350);
        Main.globalFont.draw(spriteBatch, "Final Score: " + score, 300, 300);
        Main.globalFont.getData().setScale(1);
        Main.globalFont.draw(spriteBatch, "Press ESC to return", 250, 250);
        spriteBatch.end();
    }
}
