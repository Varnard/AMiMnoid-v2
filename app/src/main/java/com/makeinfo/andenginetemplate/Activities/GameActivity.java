package com.makeinfo.andenginetemplate.Activities;

import com.makeinfo.andenginetemplate.Objects.Game;
import com.makeinfo.andenginetemplate.TextureMap;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.Scene;
import org.andengine.opengl.texture.TextureManager;
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


public class GameActivity extends SimpleBaseGameActivity implements IUpdateHandler{

    private Game game;
    private BitmapTextureAtlas atlas;

    @Override
    public void onUpdate(float pSecondsElapsed)
    {
       game.update();
    }

    @Override
    public void reset()
    {
    }

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

        scene.setBackground(new Background(0.5f,0,1));

        mEngine.registerUpdateHandler(this);

        game = new Game(scene, mEngine);
        return scene;
    }

    @Override
    protected void onCreateResources()
    {
        //BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("/");
        atlas = new BitmapTextureAtlas(getTextureManager(), 24, 24, TextureOptions.DEFAULT);
        HashMap<String,TextureRegion> textures = TextureMap.getInstance();
        TextureRegion ballTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(atlas, this, "textures.png", 0, 0);
        textures.put("ball",ballTextureRegion);
        atlas.load();
    }
}
