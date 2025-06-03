package io.github.tgam;

import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class GameLogicManager {
    private static final float WALL_TOP = Main.WALL_BOTTOM + (Main.WALL_HEIGHT_IN_BLOCKS * Main.BLOCK_HEIGHT);

    private BlockGrid blockGrid;
    private List<TextLine> fallingTextLines;
    private List<BlockChar> wallTextLines;
    private boolean gameOver;
    private float deltaTimeAccumulator;
    private int score;
    private Random random;
    private Rectangle tmpRect;

    public GameLogicManager(Random random) {
        // 初始化方块墙
        this.blockGrid = new BlockGrid(Main.WALL_HEIGHT_IN_BLOCKS, Main.WALL_WIDTH_IN_BLOCKS);

        // 初始化正在下降的文本行列表
        this.fallingTextLines = new ArrayList<>();

        // 初始化已到达墙区域的文本行列表
        this.wallTextLines = new ArrayList<>();

        this.gameOver = false;
        this.deltaTimeAccumulator = 0f;

        this.score=0;
        this.random=random;
        this.tmpRect=new Rectangle();
    }

    /**
     * 每帧调用，用于更新游戏逻辑
     */
    public void update(float deltaTime) {
        if (gameOver) return;

        deltaTimeAccumulator += deltaTime;
        if (deltaTimeAccumulator >= 1f / 60f) {
            deltaTimeAccumulator -= 1f / 60f;

            // 更新下落文本位置
            for (TextLine textLine : fallingTextLines) {
                textLine.update(deltaTime);
            }

            // 检测方块墙碰撞
            transferLinesIntoWallIfEntered();

            // 更新 wallTextLines 的位置（继续下落）
            updateWallTextLines(deltaTime);

            //碰撞处理
            handleBlockCharWallCollision();

            // 检查wallTextLine是否触底
            checkWallTextLineBottomCollision();
        }
    }

    /**
     * 检查下落字符与方块墙的碰撞（基于虚拟像素坐标）
     */
    private void transferLinesIntoWallIfEntered() {
        for (Iterator<TextLine> iterator = fallingTextLines.iterator(); iterator.hasNext();) {
            TextLine textLine = iterator.next();
            List<BlockChar> chars = textLine.getCharacters();

            if (chars.isEmpty()) {
                iterator.remove();
                continue;
            }

            BlockChar firstChar = chars.get(0);

            float topOfChar = firstChar.getY() + Main.BLOCK_HEIGHT;
            float bottomOfChar = firstChar.getY();

            // 判断第一个字符是否进入墙区域
            if (topOfChar > Main.WALL_BOTTOM && bottomOfChar < WALL_TOP) {
                wallTextLines.addAll(chars);
                iterator.remove();
            }
        }
    }

    private void handleBlockCharWallCollision(){
        if (wallTextLines.isEmpty()) return;

        for (Iterator<BlockChar> iterator = wallTextLines.iterator(); iterator.hasNext();) {
            BlockChar bc=iterator.next();
            if (blockGrid.updateWithRectangle(bc.getX(),bc.getY())) {
                iterator.remove();
            }
        }
    }

    /**
     * 检查wallTextLine中的字符是否触碰到底线
     */
    private void checkWallTextLineBottomCollision() {
        for (BlockChar ch : wallTextLines) {
            if (ch.getY() <= Main.WALL_BOTTOM) {
                gameOver = true;
                return;
            }
        }
    }

    private void updateWallTextLines(float deltaTime) {
        float dy = -50 * deltaTime; // 负值表示向下移动,可能需要重新设定速度
        for (BlockChar bc : wallTextLines) {
            bc.updateY(dy);
        }
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public List<TextLine> getFallingTextLines() {
        return fallingTextLines;
    }

    public List<BlockChar> getWallTextLines() {
        return wallTextLines;
    }

    public BlockGrid getBlockGrid() {
        return blockGrid;
    }

    /**
     * 如果 fallingTextLines 中有某一行与目标字符串完全相等，则移除该行
     * 考虑换为kmp等算法
     * @param targetText 要匹配并移除的字符串
     */
    public void removeTextLineIfMatches(String targetText) {
        Iterator<TextLine> iterator = fallingTextLines.iterator();
        while (iterator.hasNext()) {
            TextLine line = iterator.next();
            if (line.isTextEqualTo(targetText)) {
                // 文本完全匹配，移除该行
                score+=targetText.length()*10;
                iterator.remove();
                return;
            }
        }
    }

    public int getScore() {
        return score;
    }

    public void spawnFallingTextLine(String word) {
        float startX = calculateStartXForWord(word);
        float startY = Main.VIRTUAL_HEIGHT - Main.BLOCK_HEIGHT;
        float speed = 50 + random.nextFloat() * 30; // 随机速度 [50, 80]
        fallingTextLines.add(new TextLine(word, startX, startY, speed));
    }

    private float calculateStartXForWord(String word) {//需要更新
//        return (Main.VIRTUAL_WIDTH - word.length() * Main.BLOCK_WIDTH) / 2;
        int wordLength = word.length();
        if (wordLength > Main.BLOCKS_PER_ROW) {
            throw new IllegalArgumentException("Word is too long to fit in a single row.");
        }

        int blockIndexInRow = random.nextInt(Main.BLOCKS_PER_ROW - wordLength+1);
        float startX = blockIndexInRow * Main.BLOCK_WIDTH;
        return startX;
    }
}
