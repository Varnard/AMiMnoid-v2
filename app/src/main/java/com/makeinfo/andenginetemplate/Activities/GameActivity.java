package com.makeinfo.andenginetemplate.Activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import com.badlogic.gdx.math.Vector2;
import com.makeinfo.andenginetemplate.FontMap;
import com.makeinfo.andenginetemplate.MapLoader;
import com.makeinfo.andenginetemplate.Games.Game;
import com.makeinfo.andenginetemplate.Games.MirrorGame;
import com.makeinfo.andenginetemplate.Games.SnakeGame;
import com.makeinfo.andenginetemplate.TextureMap;
import com.makeinfo.andenginetemplate.Games.TimeraceGame;

import org.andengine.engine.camera.hud.HUD;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.IOnAreaTouchListener;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.ITouchArea;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.extension.physics.box2d.FixedStepPhysicsWorld;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.ui.activity.SimpleBaseGameActivity;


import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.background.Background;
import org.andengine.engine.options.ScreenOrientation;

import java.util.HashMap;
import java.util.Random;


public class GameActivity extends SimpleBaseGameActivity {

    private Game game;
    private Scene scene;
    private FixedStepPhysicsWorld physicsWorld;
    private static final int CAMERA_WIDTH = 540;
    private static final int CAMERA_HEIGHT = 960;


    @Override
    public EngineOptions onCreateEngineOptions()
    {
        Camera camera = new Camera(0, 0, GameActivity.CAMERA_WIDTH, GameActivity.CAMERA_HEIGHT);
        HUD hud = new HUD();
        camera.setHUD(hud);

        RatioResolutionPolicy ratioResolutionPolicy = new RatioResolutionPolicy(
                GameActivity.CAMERA_WIDTH, GameActivity.CAMERA_HEIGHT);
        EngineOptions engineOptions = new EngineOptions(true, ScreenOrientation.PORTRAIT_FIXED,
                                                        ratioResolutionPolicy, camera);


        return engineOptions;
    }

    @Override
    protected Scene onCreateScene()
    {
        MapLoader.setContext(this);

        scene = new Scene();
        physicsWorld = new FixedStepPhysicsWorld(30, new Vector2(0, 0), false) {
            @Override
            public void onUpdate(float pSecondsElapsed)
            {
                super.onUpdate(pSecondsElapsed);
                game.update();
            }
        };

        scene.setBackground(new Background(0.6f, 0.6f, 0.6f));

        mEngine.registerUpdateHandler(physicsWorld);


        startGame();

        return scene;
    }

    @Override
    protected void onCreateResources()
    {
        loadTextures();
        loadFonts();
    }

    protected void startGame()
    {
        Bundle parameters = getIntent().getExtras();

        int level = 1;

        if (parameters != null && parameters.containsKey("level"))
        {
            level = parameters.getInt("level");
        }

        if (parameters != null && parameters.containsKey("mode"))
        {
            if (parameters.getString("mode").equals("snake"))
            {
                game = new SnakeGame("snake", level, scene, mEngine, physicsWorld, this);
            }

            if (parameters.getString("mode").equals("timerace"))
            {
                game = new TimeraceGame("timerace", level, scene, mEngine, physicsWorld, this);
            }

            if (parameters.getString("mode").equals("mirror"))
            {
                game = new MirrorGame("mirror", level, scene, mEngine, physicsWorld, this);
            }
        }
        else game = new Game("classic", level, scene, mEngine, physicsWorld, this);
    }

    private void loadTextures()
    {
        BitmapTextureAtlas atlas = new BitmapTextureAtlas(getTextureManager(), 540, 256, TextureOptions.DEFAULT);
        HashMap<String, TextureRegion> textures = TextureMap.getInstance();
        TextureRegion hudTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(atlas, this, "textures/HUD.png", 0, 0);
        TextureRegion ballTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(atlas, this, "textures/ball.png", 0, 40);
        TextureRegion platformTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(atlas, this, "textures/platform.png", 24, 40);
        TextureRegion block1TextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(atlas, this, "textures/block1.png", 152, 40);
        TextureRegion block2TextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(atlas, this, "textures/block2.png", 216, 40);
        TextureRegion block3TextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(atlas, this, "textures/block3.png", 280, 40);
        TextureRegion block4TextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(atlas, this, "textures/block4.png", 344, 40);
        TextureRegion block5TextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(atlas, this, "textures/block5.png", 410, 40);
        TextureRegion block6TextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(atlas, this, "textures/block6.png", 474, 40);
        TextureRegion block7TextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(atlas, this, "textures/block7.png", 0, 80);
        TextureRegion block8TextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(atlas, this, "textures/block8.png", 64, 80);
        TextureRegion smallballTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(atlas, this, "textures/smallball.png", 128, 80);
        TextureRegion expandTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(atlas, this, "textures/expand.png", 146, 80);
        TextureRegion shrinkTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(atlas, this, "textures/shrink.png", 210, 80);
        TextureRegion lifeTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(atlas, this, "textures/life.png", 274, 80);
        TextureRegion dballTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(atlas, this, "textures/dball.png", 338, 80);

        textures.put("hud", hudTextureRegion);
        textures.put("ball", ballTextureRegion);
        textures.put("smallball", smallballTextureRegion);
        textures.put("platform", platformTextureRegion);
        textures.put("block1", block1TextureRegion);
        textures.put("block2", block2TextureRegion);
        textures.put("block3", block3TextureRegion);
        textures.put("block4", block4TextureRegion);
        textures.put("block5", block5TextureRegion);
        textures.put("block6", block6TextureRegion);
        textures.put("block7", block7TextureRegion);
        textures.put("block8", block8TextureRegion);
        textures.put("expand", expandTextureRegion);
        textures.put("shrink", shrinkTextureRegion);
        textures.put("life", lifeTextureRegion);
        textures.put("dball", dballTextureRegion);
        atlas.load();
    }

    private void loadFonts()
    {
        HashMap<String, Font> fonts = FontMap.getInstance();
        final ITexture fontTexture = new BitmapTextureAtlas(getTextureManager(), 256, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA);

        Font impact = FontFactory.createFromAsset(getFontManager(), fontTexture, getAssets(), "fonts/impact.ttf", 40, true, Color.BLACK);
        fonts.put("impact",impact);
        impact.load();
    }
    
    public void victory()
    {
        mEngine.unregisterUpdateHandler(physicsWorld);

        final GameActivity ga = this;
        final Text text = new Text(200, 300, FontMap.getInstance().get("impact"), "Victory!", mEngine.getVertexBufferObjectManager());
        text.setScale(3);
        text.registerUpdateHandler(new IUpdateHandler() {
            @Override
            public void onUpdate(float pSecondsElapsed)
            {
                text.setRotation(pSecondsElapsed*100);
            }

            @Override
            public void reset()
            {

            }
        });
        scene = new Scene();
        scene.setOnSceneTouchListener(new IOnSceneTouchListener() {
            @Override
            public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent)
            {
                if (pSceneTouchEvent.getMotionEvent().getAction()==TouchEvent.ACTION_DOWN)
                {
                    Intent i=new Intent(ga, MenuActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                }
                return false;
            }
        });



        scene.attachChild(text);

        scene.setBackground(new Background(0.6f, 0.8f, 0.6f));
        mEngine.setScene(scene);
    }
    
    public void gameOver()
    {
        final GameActivity ga = this;
        final Text text = new Text(200, 300, FontMap.getInstance().get("impact"), "Game Over", mEngine.getVertexBufferObjectManager());
        text.setScale(3);
        text.registerUpdateHandler(new IUpdateHandler() {
            @Override
            public void onUpdate(float pSecondsElapsed)
            {
                text.setRotation(-30+pSecondsElapsed*100);
            }

            @Override
            public void reset()
            {

            }
        });
        scene = new Scene();
        scene.setOnSceneTouchListener(new IOnSceneTouchListener() {
            @Override
            public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent)
            {
                if (pSceneTouchEvent.getMotionEvent().getAction()==TouchEvent.ACTION_DOWN)
                {
                    Intent i=new Intent(ga, MenuActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                }
                return false;
            }
        });

        scene.attachChild(text);

        scene.setBackground(new Background(0.8f, 0.6f, 0.6f));
        mEngine.setScene(scene);

    }
}
