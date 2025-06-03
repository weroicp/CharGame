package io.github.tgam;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class HistoryScreen extends BaseScreen{
    public HistoryScreen(Main game){
        super(game);
    }

    @Override
    protected void createUI(){
        Label label=new Label("History",skin);
        Table table=new Table();
        table.setFillParent(true);
        table.add(label).row();

        stage.addActor(table);

        addBackButton(Main.ScreenState.MAIN_MENU);
    }
}
