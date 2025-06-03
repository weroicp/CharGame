package io.github.tgam;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;
import java.util.List;

public class Wall {
    private List<List<Block>> blocks; // 二维结构

    public Wall(float gameWidth, float wallY, int layerCount) {
        blocks = new ArrayList<>();
        for (int row = 0; row < layerCount; row++) {
            List<Block> blockRow = new ArrayList<>();
            float y = wallY + row * Main.BLOCK_HEIGHT;
            for (int col = 0; col < gameWidth / Main.BLOCK_WIDTH; col++) {
                float x = col * Main.BLOCK_WIDTH;
                blockRow.add(new Block(x, y));
            }
            blocks.add(blockRow);
        }
    }

    public void render(SpriteBatch spriteBatch, Texture texture) {
        for (List<Block> row : blocks) {
            for (Block block : row) {
                block.draw(spriteBatch, texture);
            }
        }
    }

    /**
     * 检测 LetterBlock 是否与墙体发生碰撞
     */
    public boolean checkCollision(LetterBlock letterBlock) {
        Rectangle letterRect = letterBlock.getBoundingRectangle();
        for (List<Block> row : blocks) {
            for (Block block : row) {
                if (block.isActive && isIntersecting(letterRect, block)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * LetterBlock 碰撞后破坏对应的墙体区域
     */
    public void destroyBlocks(LetterBlock letterBlock) {
        Rectangle letterRect = letterBlock.getBoundingRectangle();
        for (List<Block> row : blocks) {
            for (Block block : row) {
                if (block.isActive && isIntersecting(letterRect, block)) {
                    block.isActive = false;
                }
            }
        }
    }

    private boolean isIntersecting(Rectangle letterRect, Block block) {
        // 创建一个与 Block 相关的 Rectangle
        Rectangle blockRect = new Rectangle(block.x, block.y, Main.BLOCK_WIDTH, Main.BLOCK_HEIGHT);
        // 使用 Rectangle 实例方法 overlaps 检查两个矩形是否相交
        return letterRect.overlaps(blockRect);
    }

    public boolean isDestroyed() {
        for (List<Block> row : blocks) {
            for (Block block : row) {
                if (block.isActive) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean isBrokenThrough(float groundY) {
        int totalRows = blocks.size();
        int totalCols = blocks.get(0).size();

        // 检查每一列
        for (int col = 0; col < totalCols; col++) {
            boolean columnDestroyed = true;

            // 检查该列的所有行（从下往上）
            for (int row = 0; row < totalRows; row++) {
                Block block = blocks.get(row).get(col);
                if (block.isActive) {
                    columnDestroyed = false;
                    break;
                }
            }

            if (columnDestroyed) {
                return true; // 存在一列完全被摧毁
            }
        }

        return false;
    }

    public float getTopY() {
        if (blocks.isEmpty()) return 0;
        return blocks.get(0).get(0).y + Main.BLOCK_HEIGHT;
    }
}
