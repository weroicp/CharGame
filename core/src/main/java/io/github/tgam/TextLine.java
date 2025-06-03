package io.github.tgam;

import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;
import java.util.List;

public class TextLine {
    private List<BlockChar> characters;
    private float speed; // 下降速度，单位是像素/秒
    private Rectangle tmpRect;

    public TextLine(String text, float startX, float startY, float speed) {
        this.speed = speed;
        this.tmpRect=new Rectangle();
        characters = new ArrayList<>();
        float currentX = startX;
        for (char c : text.toCharArray()) {
            BlockChar blockChar = new BlockChar(currentX, startY, c);
            characters.add(blockChar);
            currentX += Main.BLOCK_WIDTH;
        }
    }

    /**
     * 更新位置，基于速度和时间间隔。
     * @param deltaTime 自上一帧以来经过的时间（秒）
     */
    public void update(float deltaTime) {
        // 根据速度计算位移
        float dy = -speed * deltaTime; // 负值表示向下移动
        for (BlockChar ch : characters) {
            ch.updateY(dy);
        }
    }

    public void appendChar(char c) {
        if (!characters.isEmpty()) {
            BlockChar last = characters.get(characters.size() - 1);
            float newX = last.getX() + Main.BLOCK_WIDTH;
            float newY = last.getY();
            characters.add(new BlockChar(newX, newY, c));
        }
    }

    public List<BlockChar> getCharacters() {
        return characters;
    }

    public String getText() {
        StringBuilder sb = new StringBuilder();
        for (BlockChar ch : characters) {
            sb.append(ch.getDisplayChar());
        }
        return sb.toString();
    }

    /**
     * 判断当前文本是否等于目标字符串
     */
    public boolean isTextEqualTo(String target) {
        return getText().equals(target);
    }

    public int getLength(){
        return characters.size();
    }
}
