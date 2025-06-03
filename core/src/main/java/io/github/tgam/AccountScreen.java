package io.github.tgam;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

public class AccountScreen extends BaseScreen{
    public AccountScreen(Main game){
        super(game);
    }

    @Override
    protected void createUI(){
        Label label=new Label("Account",skin);
        Table table=new Table();
        table.setFillParent(true);
        table.add(label).row();

        stage.addActor(table);

        addBackButton(Main.ScreenState.MAIN_MENU);
    }
}
