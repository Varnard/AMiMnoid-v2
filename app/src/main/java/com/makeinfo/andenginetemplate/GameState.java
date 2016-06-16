package com.makeinfo.andenginetemplate;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.hud.HUD;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import java.util.ArrayList;

/**
 * Created by Varn on 2016-06-14.
 */
public class GameState {

    private int lives;
    private Integer score;

    private ArrayList<Sprite> balls;
    Text text;

    private HUD hud;

    public GameState(HUD hud, Engine mEngine, int lives, int score)
    {
        balls = new ArrayList<>();
        this.lives = lives;
        this.score = score;
        this.hud = hud;
        initHUD(mEngine);
    }

    public GameState(HUD hud, Engine mEngine)
    {
        balls = new ArrayList<>();
        this.lives = 0;
        this.score = 0;
        this.hud = hud;
        initHUD(mEngine);

    }

    public int getLives()
    {
        return lives;
    }

    public void setLives(int lives)
    {
        this.lives = lives;
    }

    public int getScore()
    {
        return score;
    }

    public void setScore(int score)
    {
        this.score = score;
        text.setText(this.score.toString());
    }

    public void addLife(Engine mEngine)
    {
        lives++;
        int j = balls.size();
        balls.add(new Sprite(j * 30 + 10, 10, TextureMap.getInstance().get("smallball"), mEngine.getVertexBufferObjectManager()));
        hud.attachChild(balls.get(j));
    }

    public void removeLife()
    {
        lives--;
        Sprite sprite = balls.remove(balls.size() - 1);
        hud.detachChild(sprite);
    }

    public void addScore(int score)
    {
        this.score += score;
        text.setText(this.score.toString());
    }

    private void initHUD(Engine mEngine)
    {

        VertexBufferObjectManager vboM = mEngine.getVertexBufferObjectManager();
        hud.attachChild(new Sprite(0, 0, TextureMap.getInstance().get("hud"), vboM));

        for (int i = 0; i < lives; i++)
        {
            balls.add(new Sprite(i * 30 + 10, 10, TextureMap.getInstance().get("smallball"), vboM));
            hud.attachChild(balls.get(i));
        }

        text = new Text(290, -5, FontMap.getInstance().get("impact"), "Score:0123456789", vboM);
        text.setScale(0.7f);

        text.setText(score.toString());

        hud.attachChild(text);
    }

}
