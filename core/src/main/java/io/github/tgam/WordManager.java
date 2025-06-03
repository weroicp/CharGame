package io.github.tgam;

import com.badlogic.gdx.graphics.g2d.BitmapFont;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WordManager {
    private List<LetterBlock> activeBlocks;
    private List<List<LetterBlock>> wordGroups = new ArrayList<>();
    private Random random;
    private float gameWidth;
    private float gameHeight;
    private float wallTopY;
    private float blockSpeed;
    private Wall wall;

    public WordManager(float gameWidth, float gameHeight, float wallTopY, BitmapFont font, Wall wall) {
        this.activeBlocks = new ArrayList<>();
        this.random = new Random();
        this.gameWidth = gameWidth;
        this.gameHeight = gameHeight;
        this.wallTopY = wallTopY;
        this.blockSpeed = gameHeight / 30.0f;
        this.wall = wall;
    }

    public void addLetterBlock(char letter, BitmapFont font) {
        float startX = random.nextFloat() * (gameWidth - LetterBlock.calculateWidth(font, letter));
        activeBlocks.add(new LetterBlock(letter, startX, gameHeight, blockSpeed, font));
    }

    public void addWord(String word, float startX, float startY, float speed, BitmapFont font) {
        List<LetterBlock> wordGroup = new ArrayList<>();

        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            float x = startX + i * Main.BLOCK_WIDTH;
            LetterBlock block = new LetterBlock(c, x, startY, speed, font);
            activeBlocks.add(block);
            wordGroup.add(block);
        }

        wordGroups.add(wordGroup);
    }

    public boolean eliminateWord(String inputWord) {
        String input = inputWord.trim().toUpperCase();

        for (List<LetterBlock> group : wordGroups) {
            StringBuilder sb = new StringBuilder();
            for (LetterBlock block : group) {
                if (!block.isActive()) continue;
                sb.append(block.getLetter());
            }

            String wordText = sb.toString();
            if (input.equals(wordText)) {
                // 匹配成功，消除该组所有 LetterBlock
                for (LetterBlock block : group) {
                    block.deactivate(); // 标记为不活跃
                }
                return true;
            }
        }

        return false;
    }

    public void update(float deltaTime) {
        for (LetterBlock block : activeBlocks) {
            if (block.isActive()) {
                block.update(deltaTime);

                // 检查是否碰撞到墙
                if (wall.checkCollision(block)) {
                    block.setActive(false); // 停止移动
                    wall.destroyBlocks(block); // 破坏墙体
                }

                // 如果掉出屏幕底部
                if (block.getY() + block.getHeight() < 0) {
                    block.setActive(false);
                }
            }
        }

        activeBlocks.removeIf(block -> !block.isActive());
    }

    public List<LetterBlock> getActiveBlocks() {
        return activeBlocks;
    }

    public boolean eliminateLetter(char input) {
        for (LetterBlock block : activeBlocks) {
            if (block.isActive() && block.getLetter() == input) {
                block.setActive(false);
                return true;
            }
        }
        return false;
    }
}
