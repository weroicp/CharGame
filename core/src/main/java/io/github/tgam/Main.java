package io.github.tgam;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {
    public static final float GAME_HEIGHT = 800; // 屏幕高度
    public static final float GAME_WIDTH = 480; // 屏幕高度

    public static BitmapFont globalFont; // 全局字体对象
    public static Texture WALL_TEXTURE; // 墙体的纹理图

    //虚拟BLOCK 宽高（单位：像素）
    public static float BLOCK_WIDTH=16;
    public static float BLOCK_HEIGHT=16;

    //虚拟屏幕相关参数
    public static final int BLOCKS_PER_ROW = 30;//width
    public static final int BLOCKS_PER_COLUMN = 50;//height
    public static final float VIRTUAL_WIDTH=BLOCK_WIDTH*BLOCKS_PER_ROW;
    public static final float VIRTUAL_HEIGHT=BLOCK_WIDTH*BLOCKS_PER_COLUMN;

    //方块墙相关参数
    public static final int WALL_HEIGHT_IN_BLOCKS = 5;//方块墙单位高度
    public static final int WALL_WIDTH_IN_BLOCKS = BLOCKS_PER_ROW;//方块墙单位宽度
    public static final float WALL_BOTTOM = 8; // 方块墙底部 Y 坐标

    private Stage currentStage;
    private BaseScreen currentScreen;
    public static Skin skin;

    protected enum ScreenState{
        MAIN_MENU,ACCOUNT,GAME,MULTIPLAYER,HISTORY,SETTINGS
    }
    private ScreenState currentState=ScreenState.MAIN_MENU;

    @Override
    public void create() {
        // 初始化字体（全局）
        globalFont = new BitmapFont(); // 使用默认字体即可

        // 初始化方块纹理
        updateBlockTexture();

        skin=new Skin(Gdx.files.internal("uiskin.json"));
        switchScreen(ScreenState.MAIN_MENU);
    }

    @Override
    public void render() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        currentScreen.render(Gdx.graphics.getDeltaTime());
    }

    @Override
    public void dispose() {
        if (currentScreen != null) {
            currentScreen.dispose();
        }
        if (skin != null) {
            skin.dispose();
        }
        if (WALL_TEXTURE != null) {
            WALL_TEXTURE.dispose();
        }
        if (globalFont != null) {
            globalFont.dispose(); // 释放字体资源
        }
    }

    protected void switchScreen(ScreenState newState){
        if(currentScreen!=null){
            currentScreen.dispose();
        }

        switch (newState){
            case MAIN_MENU:
                currentScreen = new MainMenuScreen(this);
                break;
            case ACCOUNT:
                currentScreen = new AccountScreen(this);
                break;
            case GAME:
                currentScreen = new GameScreen(this);
                break;
            case MULTIPLAYER:
                currentScreen = new MultiplayerScreen(this);
                break;
            case HISTORY:
                currentScreen = new HistoryScreen(this);
                break;
            case SETTINGS:
                currentScreen = new SettingsScreen(this);
                break;
        }

        currentState=newState;
        currentStage=currentScreen.getStage();
        Gdx.input.setInputProcessor(currentStage);
    }

    public static float calculateBlockWidth() {
        GlyphLayout layout = new GlyphLayout();
        layout.setText(globalFont, "W"); // 使用典型字母估算平均字符宽度
        return layout.width;
    }

    public static float calculateBlockHeight() {
        return globalFont.getCapHeight(); // 获取字符高度
    }

    public static void updateBlockTexture() {
        if (WALL_TEXTURE != null) {
            WALL_TEXTURE.dispose();
        }

        int blockWidth = (int) calculateBlockWidth();
        int blockHeight = (int) calculateBlockHeight();

//        Pixmap pixmap = new Pixmap(blockWidth, blockHeight, Pixmap.Format.RGBA8888);
//        pixmap.setColor(Color.BROWN); // 更直观表示防守墙
//        pixmap.fill();
//        pixelTexture = new Texture(pixmap);
//        pixmap.dispose();
        WALL_TEXTURE=new Texture("blockg.png");
    }

    @Override
    public void resize(int width, int height) {
        if (currentScreen != null) {
            currentScreen.resize(width, height);
        }
    }
}
