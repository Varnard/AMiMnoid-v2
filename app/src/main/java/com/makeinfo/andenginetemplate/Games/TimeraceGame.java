package com.makeinfo.andenginetemplate.Games;

import android.content.Context;

import com.badlogic.gdx.physics.box2d.BodyDef;

import org.andengine.engine.Engine;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.Scene;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;

public class TimeraceGame extends Game {

    int timer;
    Rectangle limit;

    public TimeraceGame(String mode, int level, Scene scene, final Engine mEngine, PhysicsWorld physicsWorld, Context context)
    {
        super(mode, level, scene, mEngine, physicsWorld, context);
        timer = 0;
        limit = new Rectangle(0, 40, mEngine.getCamera().getWidth(), 1, mEngine.getVertexBufferObjectManager());
        limit.setColor(0.3f, 0.3f, 0.3f);
        scene.attachChild(limit);
        ceiling = PhysicsFactory.createBoxBody(physicsWorld, mEngine.getCamera().getWidth()/ 2, 40, mEngine.getCamera().getWidth(), 1, BodyDef.BodyType.KinematicBody, WALL_FIXTURE_DEF);
        ceiling.setUserData("ceiling");
        ceiling.setLinearVelocity(0,0.1f);
    }

    @Override
    protected synchronized void updateObjects()
    {
        super.updateObjects();
        moveBlocks();
    }

    private void moveBlocks()
    {
        limit.setHeight(limit.getHeight()+0.05f);
        for (int i = 0; i < 16; i++)
        {
            for (int j = 0; j < 8; j++)
            {
                if (blocks[i][j] != null)
                {
                    blocks[i][j].getBody().setLinearVelocity(0, 0.1f);
                }
            }
        }

    }
}
