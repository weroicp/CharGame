package io.github.tgam;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public abstract class BaseScreen {
    protected final Stage stage;
    protected Skin skin;
    protected final Main game;
    protected final Viewport viewport;
    protected final OrthographicCamera camera;

    public BaseScreen(Main game){
        this.game=game;
        this.skin=Main.skin;

        // 初始化摄像机
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Main.GAME_WIDTH, Main.GAME_HEIGHT);
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);

        viewport=new FitViewport(Main.GAME_WIDTH,Main.GAME_HEIGHT,camera);
        this.stage=new Stage(viewport);
        createUI();
    }
    protected abstract void createUI();

    public Stage getStage(){
        return this.stage;
    }
    protected void addBackButton(Main.ScreenState targetState){
        TextButton backButton=new TextButton("Back to MainMenu",skin);
        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                game.switchScreen(targetState);
            }
        });
        Table table=new Table();
        table.setFillParent(true);
        table.bottom().left();
        table.add(backButton).pad(10);

        stage.addActor(table);
    }
    protected TextButton createButton(String text, final Main.ScreenState targetState) {
        TextButton button = new TextButton(text, skin);
        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.switchScreen(targetState);
            }
        });
        return button;
    }

    public void render(float delta){
        // Step 1: 处理UI交互逻辑
        stage.act(delta);

        // Step 2: 子类实现自定义渲染（如背景、游戏预览）
        renderUI(delta);

        // Step 3: 绘制UI元素
        stage.draw();
    }

    protected void renderUI(float delta){

    }

    public void dispose(){
        stage.dispose();
    }

    public void resize(int width, int height) {
        viewport.update(width, height);
        viewport.apply();
    }
}
