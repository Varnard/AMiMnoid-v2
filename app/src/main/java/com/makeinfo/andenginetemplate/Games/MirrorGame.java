package com.makeinfo.andenginetemplate.Games;

import android.content.Context;

import com.makeinfo.andenginetemplate.Objects.Ball;
import com.makeinfo.andenginetemplate.Objects.Platform;

import org.andengine.engine.Engine;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

public class MirrorGame extends Game {

    Platform mirrorPlatform;

    public MirrorGame(String mode, int level, Scene scene, final Engine mEngine, PhysicsWorld physicsWorld, Context context)
    {
        super(mode, level, scene, mEngine, physicsWorld, context);
    }

    @Override
    protected void createWalls(float width, float height)
    {
        super.createWalls(width, height);
        ceiling.setUserData("ground");
    }

    @Override
    protected void createPlatform(float width, float height, VertexBufferObjectManager vboManager)
    {
        super.createPlatform(width, height, vboManager);
        mirrorPlatform = new Platform(width / 2, 100, vboManager, physicsWorld);
        scene.attachChild(mirrorPlatform);
    }


    @Override
    protected synchronized void updateObjects()
    {
        super.updateObjects();
        updatePlatform(mirrorPlatform);

    }

    @Override
    protected void destroyPlatform()
    {
        super.destroyPlatform();
        mirrorPlatform.getBody().setActive(false);
        scene.detachChild(mirrorPlatform);
    }

    @Override
    protected IOnSceneTouchListener createTouchListener(final float width)
    {
        return new IOnSceneTouchListener() {
            @Override
            public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent)
            {
                float touchX = pSceneTouchEvent.getX();

                if (touchX > width - 1 - platform.getWidth() / 2)
                {
                    platform.setTarget(width - platform.getWidth() / 2 - 1);
                    mirrorPlatform.setTarget(width - platform.getWidth() / 2 - 1);
                }
                else if (touchX < platform.getWidth() / 2 + 1)
                {
                    platform.setTarget(platform.getWidth() / 2 + 1);
                    mirrorPlatform.setTarget(platform.getWidth() / 2 + 1);
                }
                else
                {
                    platform.setTarget(touchX);
                    mirrorPlatform.setTarget(touchX);
                }

                if (pSceneTouchEvent.isActionUp())
                {
                    platform.getBody().setUserData("launch");
                    mirrorPlatform.getBody().setUserData("launch");
                }

                return false;


            }
        };
    }

}
