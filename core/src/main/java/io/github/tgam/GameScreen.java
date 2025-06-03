package io.github.tgam;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.Random;

public class GameScreen extends BaseScreen{
    private final GameRenderer renderer;
    private final GameLogicManager logicManager;

    // 游戏相关变量
    private Random random;
    private StringBuilder inputText = new StringBuilder();

    private long lastSpawnTime;
    private boolean gameOver = false;

    private final String[] wordBank = {"HELLO", "WORLD", "GAME", "CODE", "JAVA", "ANDROID", "LIBGDX"};

    public GameScreen(Main game) {
        super(game);

        // 初始化渲染器
        renderer = new GameRenderer(camera,viewport);

        // 初始化逻辑管理器
        random=new Random();
        logicManager = new GameLogicManager(random);

        // 初始生成时间
        lastSpawnTime = TimeUtils.nanoTime();
    }

    @Override
    protected void createUI() {
        Table table = new Table();
        table.setFillParent(true);
        table.bottom().padBottom(Main.WALL_BOTTOM);
        stage.addActor(table);
    }

    @Override
    public void dispose() {
        renderer.dispose();
        super.dispose();
    }

    @Override
    public void renderUI(float delta) {
        //清屏
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        handleInput(); // 自定义输入处理
        if (!gameOver) {
            updateGame(delta);
        }

        renderer.render(delta,logicManager,inputText.toString());

//        // 显示得分和输入
//        font.draw(spriteBatch, "Score: " + score, 600, 580);
//        font.draw(spriteBatch, "Input: " + inputText.toString(), 10, 580);

        if (gameOver) {
            renderer.drawGameOverScreen(logicManager.getScore());
        }
    }

    private void handleInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            // 返回主菜单或其他退出逻辑
            game.switchScreen(Main.ScreenState.MAIN_MENU);
            return; // 提前退出，防止继续处理其他输入
        }

        boolean isShiftPressed = Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT) || Gdx.input.isKeyPressed(Input.Keys.SHIFT_RIGHT);

        if (Gdx.input.isKeyJustPressed(Input.Keys.BACKSPACE)) {
            if (inputText.length() > 0) {
                inputText.deleteCharAt(inputText.length() - 1); // 删除最后一个字符
            }
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER) || Gdx.input.isKeyJustPressed(Input.Keys.NUMPAD_ENTER)) {
            String currentInput = inputText.toString().trim();
            if (!currentInput.isEmpty()) {
                logicManager.removeTextLineIfMatches(currentInput);
                inputText.setLength(0); // 清空输入
            }
        } else {
            // 处理字母键
            for (int i = Input.Keys.A; i <= Input.Keys.Z; i++) {
                if (Gdx.input.isKeyJustPressed(i)) {
                    char c = (char) ('a' + (i - Input.Keys.A)); // 获取小写字母

                    // 根据Shift键状态调整大小写
                    if (isShiftPressed) {
                        c = Character.toUpperCase(c);
                    }

                    inputText.append(c);
                    break;
                }
            }

            // 可选：处理数字键
            for (int i = Input.Keys.NUM_0; i <= Input.Keys.NUM_9; i++) {
                if (Gdx.input.isKeyJustPressed(i)) {
                    char c = (char) ('0' + (i - Input.Keys.NUM_0));
                    inputText.append(c);
                    break;
                }
            }

            for (int i = Input.Keys.NUMPAD_0; i <= Input.Keys.NUMPAD_9; i++) {
                if (Gdx.input.isKeyJustPressed(i)) {
                    char c = (char) ('0' + (i - Input.Keys.NUMPAD_0));
                    inputText.append(c);
                    break;
                }
            }
        }
    }

    private void updateGame(float deltaTime) {
       logicManager.update(deltaTime);

        // 检查是否墙体被穿透
        if (logicManager.isGameOver()) {
            gameOver = true;
        }

        // 定时生成新单词
        if (TimeUtils.nanoTime() - lastSpawnTime > 4_000_000_000L) { // 每5秒生成一个单词
            String word = pickRandomWord();

            logicManager.spawnFallingTextLine(word);
            lastSpawnTime = TimeUtils.nanoTime();
        }
    }

    private String pickRandomWord() {
        return wordBank[random.nextInt(wordBank.length)];
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height); // 更新 FitViewport
        camera.position.set(Main.GAME_WIDTH / 2f, Main.GAME_HEIGHT / 2f, 0); // 可选：重新居中
        camera.update();

        // 同步更新 Stage 的视口
        stage.getViewport().update(width, height, true);
    }
}
