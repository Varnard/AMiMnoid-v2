package com.makeinfo.andenginetemplate.Activities;

import android.os.Bundle;

import com.badlogic.gdx.math.Vector2;
import com.makeinfo.andenginetemplate.MapLoader;
import com.makeinfo.andenginetemplate.Games.Game;
import com.makeinfo.andenginetemplate.Games.MirrorGame;
import com.makeinfo.andenginetemplate.Games.SnakeGame;
import com.makeinfo.andenginetemplate.TextureMap;
import com.makeinfo.andenginetemplate.Games.TimeraceGame;

import org.andengine.engine.options.EngineOptions;
import org.andengine.entity.scene.Scene;
import org.andengine.extension.physics.box2d.FixedStepPhysicsWorld;
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


public class GameActivity extends SimpleBaseGameActivity {

    private Game game;
    private static final int CAMERA_WIDTH = 540;
    private static final int CAMERA_HEIGHT = 960;


    @Override
    public EngineOptions onCreateEngineOptions()
    {
        Camera camera = new Camera(0, 0, GameActivity.CAMERA_WIDTH, GameActivity.CAMERA_HEIGHT);

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

        Scene scene = new Scene();
        FixedStepPhysicsWorld physicsWorld = new FixedStepPhysicsWorld(30, new Vector2(0, 0), false) {
            @Override
            public void onUpdate(float pSecondsElapsed) {
                super.onUpdate(pSecondsElapsed);
                game.update();
            }
        };

        scene.setBackground(new Background(0.6f,0.6f,0.6f));

        mEngine.registerUpdateHandler(physicsWorld);

        Bundle parameters = getIntent().getExtras();
        if(parameters != null && parameters.containsKey("mode"))
        {
            if (parameters.getString("mode").equals("snake"))
            {
                game = new SnakeGame("snake",1,scene, mEngine, physicsWorld);
            }

            if (parameters.getString("mode").equals("timerace"))
            {
                game = new TimeraceGame("timerace",1,scene, mEngine, physicsWorld);
            }

            if (parameters.getString("mode").equals("mirror"))
            {
                game = new MirrorGame("mirror",1,scene, mEngine, physicsWorld);
            }
        }
        else game = new Game("classic",1,scene, mEngine, physicsWorld);

        return scene;
    }

    @Override
    protected void onCreateResources()
    {
        BitmapTextureAtlas atlas = new BitmapTextureAtlas(getTextureManager(), 128, 128, TextureOptions.DEFAULT);
        HashMap<String,TextureRegion> textures = TextureMap.getInstance();
        TextureRegion ballTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(atlas, this, "textures/ball.png",0,0);
        TextureRegion platformTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(atlas, this, "textures/platform.png", 0, 24);
        TextureRegion block1TextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(atlas, this, "textures/block1.png",0,48);
        TextureRegion block2TextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(atlas, this, "textures/block2.png",0,72);
        TextureRegion block3TextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(atlas, this, "textures/block3.png",0,96);
        textures.put("ball",ballTextureRegion);
        textures.put("platform",platformTextureRegion);
        textures.put("block1",block1TextureRegion);
        textures.put("block2",block2TextureRegion);
        textures.put("block3",block3TextureRegion);
        atlas.load();
    }


}
