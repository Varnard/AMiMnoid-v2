package com.makeinfo.andenginetemplate.Games;

import org.andengine.engine.Engine;
import org.andengine.entity.scene.Scene;
import org.andengine.extension.physics.box2d.PhysicsWorld;

public class TimeraceGame extends Game {

    int timer;

    public TimeraceGame(String mode, int level, Scene scene, final Engine mEngine, PhysicsWorld physicsWorld)
    {
        super(mode,level,scene,mEngine,physicsWorld);
        timer=0;
    }

    @Override
    protected synchronized void updateObjects()
    {
        super.updateObjects();
        moveBlocks();
    }

    private void moveBlocks()
    {

            for (int i = 0; i < 16; i++)
            {
                for (int j = 0; j < 8; j++)
                {
                        blocks[i][j].getBody().setLinearVelocity(0, 0.1f);
                }
            }

    }
}
