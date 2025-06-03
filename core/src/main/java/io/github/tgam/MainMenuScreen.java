package io.github.tgam;

import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class MainMenuScreen extends BaseScreen{
    public MainMenuScreen(Main game){
        super(game);
    }

    @Override
    protected void createUI(){
        Table table=new Table();
        table.setFillParent(true);
        table.left().center();
        table.padLeft(5);

        table.add(createButton("Account", Main.ScreenState.ACCOUNT)).fillX().pad(10).row();
        table.add(createButton("Game", Main.ScreenState.GAME)).fillX().pad(10).row();
        table.add(createButton("Multiplayer", Main.ScreenState.MULTIPLAYER)).fillX().pad(10).row();
        table.add(createButton("History", Main.ScreenState.HISTORY)).fillX().pad(10).row();
        table.add(createButton("Settings", Main.ScreenState.SETTINGS)).fillX().pad(10).row();

        stage.addActor(table);
    }
}
