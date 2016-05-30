package com.makeinfo.andenginetemplate.Objects;


import org.andengine.engine.Engine;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.input.touch.TouchEvent;

public class Game {

    Platform platform;
    Ball ball;
    Block[][] blocks;

    private Scene scene;
    private Engine mEngine;

    public Game(Scene scene, final Engine mEngine)
    {
        this.scene=scene;
        this.mEngine=mEngine;
        platform = new Platform(mEngine.getCamera().getWidth()/2,
                            mEngine.getCamera().getHeight()-160,
                            120,20,
                            this.mEngine.getVertexBufferObjectManager());

        ball = new Ball(270,600,this.mEngine.getVertexBufferObjectManager());

        int[][] z= {
                {1, 2, 1, 3, 3, 1, 2, 1},
                {1, 2, 1, 3, 3, 1, 2, 1},
                {1, 2, 1, 3, 3, 1, 2, 1},
                {1, 2, 1, 3, 3, 1, 2, 1},
                {1, 2, 1, 3, 3, 1, 2, 1},
                {1, 2, 1, 3, 3, 1, 2, 1},
                {1, 2, 1, 3, 3, 1, 2, 1},
                {1, 2, 1, 3, 3, 1, 2, 1},
                {1, 2, 1, 3, 3, 1, 2, 1},
                {1, 2, 1, 3, 3, 1, 2, 1},
                {1, 2, 1, 3, 3, 1, 2, 1},
                {1, 2, 1, 3, 3, 1, 2, 1},
                {1, 2, 1, 3, 3, 1, 2, 1},
                {1, 2, 1, 3, 3, 1, 2, 1},
                {1, 2, 1, 3, 3, 1, 2, 1},
                {1, 2, 1, 3, 3, 1, 2, 1},
                {1, 2, 1, 3, 3, 1, 2, 1},
                {1, 2, 1, 3, 3, 1, 2, 1},
                {1, 2, 1, 3, 3, 1, 2, 1},
                {1, 2, 1, 3, 3, 1, 2, 1}
        };
        blocks = new Block[20][8];
        float bsX = mEngine.getCamera().getWidth()/8;
        float bsY = 20;
        for (int i=0;i<8;i++)
        {
            for (int j=0;j<20;j++)
            {
                blocks[j][i]=new Block(i*bsX,j*20,bsX-1,bsY-1,this.mEngine.getVertexBufferObjectManager(),z[j][i]);
                scene.attachChild(blocks[j][i]);
            }

        }

        scene.attachChild(platform);
        scene.attachChild(ball);

        scene.setOnSceneTouchListener(new IOnSceneTouchListener() {
            @Override
            public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent)
            {
                float touchX = pSceneTouchEvent.getX();

                if(touchX>mEngine.getCamera().getWidth()- platform.getWidth()/2)
                {
                    platform.setTarget(mEngine.getCamera().getWidth()- platform.getWidth());
                }
                else if(touchX< platform.getWidth()/2) platform.setTarget(0);
                else platform.setTarget(touchX- platform.getWidth()/2);

                return false;
            }
        });
    }

    private void checkPlatformCollision(Ball ball)
    {
        if (ball.collidesWith(platform))
        {
            double angle =(((ball.getX()+ball.getWidth()/2)-(platform.getX()+platform.getWidth()/2))/(platform.getWidth()/2)*90);
            ball.setSpeed(angle);

        }
    }

    private void checkWallCollision(Ball ball)
    {
        if (ball.getX()<0)
        {
            ball.setX(0);
            ball.reverseSpeedX();
        } 
        else if (ball.getX()>mEngine.getCamera().getWidth()-ball.getWidth()) 
        {
            ball.setX(mEngine.getCamera().getWidth()-ball.getWidth());
            ball.reverseSpeedX();
        }

        if (ball.getY()<0)
        {
            ball.setY(0);
            ball.reverseSpeedY();
        }
        else if (ball.getY()>mEngine.getCamera().getHeight()-ball.getHeight())
        {
            ball.setY(mEngine.getCamera().getHeight()-ball.getHeight());
            ball.reverseSpeedY();
        }
        
        
    }

    public void update()
    {
        platform.move();
        checkWallCollision(ball);
        checkPlatformCollision(ball);
        ball.move();
    }
}
