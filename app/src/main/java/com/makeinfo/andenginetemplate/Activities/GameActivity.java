package com.makeinfo.andenginetemplate.Activities;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.makeinfo.andenginetemplate.Objects.Game;
import com.makeinfo.andenginetemplate.TextureMap;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.Scene;
import org.andengine.extension.physics.box2d.FixedStepPhysicsWorld;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.ui.activity.SimpleBaseGameActivity;



import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.background.Background;
import org.andengine.engine.options.ScreenOrientation;

import java.util.HashMap;


public class GameActivity extends SimpleBaseGameActivity {

    private Game game;
    private FixedStepPhysicsWorld physicsWorld;
    private Camera camera;
    private static final int CAMERA_WIDTH = 540;
    private static final int CAMERA_HEIGHT = 960;


    @Override
    public EngineOptions onCreateEngineOptions()
    {
        camera = new Camera(0, 0, GameActivity.CAMERA_WIDTH, GameActivity.CAMERA_HEIGHT);

        RatioResolutionPolicy ratioResolutionPolicy = new RatioResolutionPolicy(
                GameActivity.CAMERA_WIDTH, GameActivity.CAMERA_HEIGHT);
        EngineOptions engineOptions = new EngineOptions(true, ScreenOrientation.PORTRAIT_FIXED,
                ratioResolutionPolicy, camera);


        return engineOptions;
    }

    @Override
    protected Scene onCreateScene()
    {
        Scene scene = new Scene();
        physicsWorld = new FixedStepPhysicsWorld(30,new Vector2(0,0),false)
        {
            @Override
            public void onUpdate(float pSecondsElapsed)
            {
                super.onUpdate(pSecondsElapsed);
                game.update();
            }
        };

        scene.setBackground(new Background(0.5f,0,1));

        mEngine.registerUpdateHandler(physicsWorld);


        game = new Game(scene, mEngine, physicsWorld);
        return scene;
    }

    @Override
    protected void onCreateResources()
    {
        BitmapTextureAtlas atlas = new BitmapTextureAtlas(getTextureManager(), 128, 64, TextureOptions.DEFAULT);
        HashMap<String,TextureRegion> textures = TextureMap.getInstance();
        TextureRegion ballTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(atlas, this, "ball.png",0,0);
        TextureRegion platformTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(atlas, this, "platform.png", 0, 32);
        textures.put("ball",ballTextureRegion);
        textures.put("platform",platformTextureRegion);
        atlas.load();
    }


}
