package com.makeinfo.andenginetemplate.Games;

import org.andengine.engine.Engine;
import org.andengine.entity.scene.Scene;
import org.andengine.extension.physics.box2d.PhysicsWorld;

public class SnakeGame extends Game {

    int timer;

    public SnakeGame(String mode, int level, Scene scene, final Engine mEngine, PhysicsWorld physicsWorld)
    {
        super(mode, level, scene, mEngine, physicsWorld);
        timer = 0;
    }

    @Override
    protected synchronized void updateObjects()
    {
        super.updateObjects();
        moveBlocks();
    }


    private void moveBlocks()
    {
        if (timer < 150 || timer > 450)
        {
            for (int i = 0; i < 16; i++)
            {
                for (int j = 0; j < 8; j++)
                {
                    if (blocks[i][j] != null)
                    {
                        if (i % 2 == 0)
                            blocks[i][j].getBody().setLinearVelocity(2, 0);
                        else blocks[i][j].getBody().setLinearVelocity(-2, 0);
                    }
                }
            }
        }
        else
        {
            for (int i = 0; i < 16; i++)
            {
                for (int j = 0; j < 8; j++)
                {
                    if (blocks[i][j] != null)
                    {
                        if (i % 2 == 0)
                            blocks[i][j].getBody().setLinearVelocity(-2, 0);
                        else blocks[i][j].getBody().setLinearVelocity(2, 0);
                    }
                }
            }
        }
        if (timer == 600) timer = 0;
        timer++;
    }
}
