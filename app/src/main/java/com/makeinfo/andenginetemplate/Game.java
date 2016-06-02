package com.makeinfo.andenginetemplate;


import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.makeinfo.andenginetemplate.MapLoader;
import com.makeinfo.andenginetemplate.Objects.Ball;
import com.makeinfo.andenginetemplate.Objects.Block;
import com.makeinfo.andenginetemplate.Objects.Platform;

import org.andengine.engine.Engine;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsConnectorManager;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import java.util.ArrayList;
import java.util.List;

public class Game {

    protected Platform platform;
    protected ArrayList<Ball> balls;
    protected Block[][] blocks;
    private Body leftWall;
    private Body rightWall;
    private Body ceiling;
    private Body ground;

    private Scene scene;
    private Engine mEngine;
    private PhysicsWorld physicsWorld;

    /* The categories. */
    public static final short CATEGORYBIT_WALL = 1;
    public static final short CATEGORYBIT_PlATFORM = 2;
    public static final short CATEGORYBIT_BLOCK = 4;
    public static final short CATEGORYBIT_BALL = 8;

    /* And what should collide with what. */
    public static final short MASKBITS_WALL = CATEGORYBIT_BALL;
    public static final short MASKBITS_PlATFORM = CATEGORYBIT_BALL;
    public static final short MASKBITS_BLOCK = CATEGORYBIT_BALL;
    public static final short MASKBITS_BALL = CATEGORYBIT_WALL + CATEGORYBIT_PlATFORM + CATEGORYBIT_BLOCK;

    public static final FixtureDef WALL_FIXTURE_DEF = PhysicsFactory.createFixtureDef(1, 1, 0, false, CATEGORYBIT_WALL, MASKBITS_WALL, (short)0);
    public static final FixtureDef PLATFORM_FIXTURE_DEF = PhysicsFactory.createFixtureDef(1, 1, 0, false, CATEGORYBIT_PlATFORM, MASKBITS_PlATFORM, (short)0);
    public static final FixtureDef BLOCK_FIXTURE_DEF = PhysicsFactory.createFixtureDef(1, 1, 0, false, CATEGORYBIT_BLOCK, MASKBITS_BLOCK, (short)0);
    public static final FixtureDef BALL_FIXTURE_DEF = PhysicsFactory.createFixtureDef(1, 1, 0, false, CATEGORYBIT_BALL, MASKBITS_BALL, (short)0);


    public Game(Scene scene, final Engine mEngine, PhysicsWorld physicsWorld)
    {
        physicsWorld.setContactListener(createContactListener());

        this.scene=scene;
        this.mEngine=mEngine;
        this.physicsWorld=physicsWorld;

        final float height = mEngine.getCamera().getHeight();
        final float width = mEngine.getCamera().getWidth();

        VertexBufferObjectManager vboManager = mEngine.getVertexBufferObjectManager();

        balls=new ArrayList<>();

        leftWall= PhysicsFactory.createBoxBody(physicsWorld,0,height/2,1,height, BodyDef.BodyType.StaticBody,WALL_FIXTURE_DEF);
        leftWall.setUserData("sideWall");
        rightWall= PhysicsFactory.createBoxBody(physicsWorld,width,height/2,1,height, BodyDef.BodyType.StaticBody,WALL_FIXTURE_DEF);
        rightWall.setUserData("sideWall");
        ceiling= PhysicsFactory.createBoxBody(physicsWorld,width/2,0,width,1,BodyDef.BodyType.StaticBody,WALL_FIXTURE_DEF);
        ceiling.setUserData("ceiling");
        ground= PhysicsFactory.createBoxBody(physicsWorld,width/2,height,width,1,BodyDef.BodyType.StaticBody,WALL_FIXTURE_DEF);
        ground.setUserData("ground");

        platform = new Platform(width/2,height-160,vboManager,physicsWorld);

        Ball startBall = new Ball(270,800,120,vboManager,physicsWorld);
        balls.add(startBall);
        platform.attachBall(startBall);

        /*Ball startBall2 = new Ball(270,600,120,vboManager,physicsWorld);
        balls.add(startBall2);
        Ball startBall3 = new Ball(400,700,120,vboManager,physicsWorld);
        balls.add(startBall3);
        Ball startBall4 = new Ball(70,800,120,vboManager,physicsWorld);
        balls.add(startBall4);*/


        int[][] z= MapLoader.getLevel("asdf",0);

        blocks = new Block[16][8];
        float spacingX = (mEngine.getCamera().getWidth()-64*8)/9;
        float spacingY = 2;
        for (int i=0;i<8;i++)
        {
            for (int j=0;j<16;j++)
            {
                switch (z[j][i])
                {
                    case 1:
                    {
                        blocks[j][i] = new Block(i * (64+spacingX) , j * (24+spacingY)+2, "block1", vboManager, physicsWorld);
                        break;
                    }

                    case 2:
                    {
                        blocks[j][i] = new Block(i * (64+spacingX) , j * (24+spacingY)+2, "block2", vboManager, physicsWorld);
                        break;
                    }

                    case 3:
                    {
                        blocks[j][i] = new Block(i * (64+spacingX) , j * (24+spacingY)+2, "block3", vboManager, physicsWorld);
                        break;
                    }

                }

                scene.attachChild(blocks[j][i]);
            }

        }

        scene.attachChild(platform);

        for (Ball ball : balls)
        {
            scene.attachChild(ball);
        }

        scene.setOnSceneTouchListener(new IOnSceneTouchListener() {
            @Override
            public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent)
            {
                float touchX = pSceneTouchEvent.getX();

                if(touchX>width - 1 - platform.getWidth()/2)
                {
                    platform.setTarget(width- platform.getWidth()/2-1);
                }
                else if(touchX< platform.getWidth()/2+1) platform.setTarget(platform.getWidth()/2+1);
                else platform.setTarget(touchX);

                if (pSceneTouchEvent.isActionUp())
                {
                    platform.getBody().setUserData("launch");
                }

                return false;


            }
        });

    }



    public void update()
    {
        platform.move();
        updateObjects();
        if (noBallsLeft())softReset();
    }


    public boolean noBallsLeft()
    {
        return balls.isEmpty();
    }
    public synchronized void softReset()
    {
        platform.getBody().setActive(false);
        scene.detachChild(platform);


        for (Ball ball : balls)
        {
            scene.detachChild(ball);
            ball.getBody().setActive(false);
            balls.remove(ball);
        }

        platform = new Platform(270,800,mEngine.getVertexBufferObjectManager(),physicsWorld);
        scene.attachChild(platform);

        Ball startBall = new Ball(270,800,120,mEngine.getVertexBufferObjectManager(),physicsWorld);
        balls.add(startBall);

        for (Ball ball : balls)
        {
            scene.attachChild(ball);
        }
    }

    protected synchronized void updateObjects()
    {
        for (int i=0;i<8;i++)
        {
            for (int j=0;j<16;j++)
            {
                if (blocks[j][i].getBody().getUserData().equals("destroyed"))
                {
                    final Block block = blocks[j][i];
                    mEngine.runOnUpdateThread(new Runnable()
                    {
                        @Override
                        public void run() {
                            block.getBody().setActive(false);
                            scene.detachChild(block);
                        }
                    });
                }
            }
        }

        for (Ball ball : balls)
        {
            if (ball.getBody().getUserData().equals("destroyed"))
            {
                final PhysicsConnector physicsConnector =
                        physicsWorld.getPhysicsConnectorManager().findPhysicsConnectorByShape(ball);
                final Ball b=ball;

                mEngine.runOnUpdateThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        if (physicsConnector != null)
                        {
                            balls.remove(b);
                            physicsWorld.unregisterPhysicsConnector(physicsConnector);
                            b.getBody().setActive(false);
                            physicsWorld.destroyBody(b.getBody());
                            scene.detachChild(b);
                        }
                    }
                });
            }

            if (ball.getBody().getUserData().equals("attach"))
            {
                final Ball b = ball;
                mEngine.runOnUpdateThread(new Runnable()
                {
                    @Override
                    public void run() {
                        platform.attachBall(b);
                    }
                });

            }
        }

        if (platform.getBody().getUserData().equals("launch"))
        {
            platform.getBody().setUserData("platform");
            platform.setMagnet(false);
            final ArrayList<Ball> attachedBalls = platform.getAttachedBalls();
            for (Ball ball : attachedBalls)
            {
                final Ball b = ball;
                mEngine.runOnUpdateThread(new Runnable()
                {
                    @Override
                    public void run() 
                    {
                    
                        double angle = 0.8 * ((b.getBody().getPosition().x - platform.getBody().getPosition().x) * 32 / (platform.getWidth() / 2) * 90);
                        b.getBody().setLinearVelocity(Ball.computeVelocity(180 - angle));
                        attachedBalls.remove(b);
                        b.getBody().setUserData("ball");
                    }
                });
            }
        }

    }


    protected ContactListener createContactListener()
    {
        ContactListener contactListener = new ContactListener()
        {
            @Override
            public synchronized void beginContact(Contact contact)
            {
                final Fixture x1 = contact.getFixtureA();
                final Fixture x2 = contact.getFixtureB();

                if (x2.getBody().getUserData().equals("ball")&&x1.getBody().getUserData().equals("sideWall"))
                {
                    x2.getBody().setLinearVelocity(-(x2.getBody().getLinearVelocity().x),x2.getBody().getLinearVelocity().y);
                }


                if (x2.getBody().getUserData().equals("ball")&&x1.getBody().getUserData().equals("ceiling"))
                {
                    x2.getBody().setLinearVelocity(x2.getBody().getLinearVelocity().x,-(x2.getBody().getLinearVelocity().y));
                }

                if (x2.getBody().getUserData().equals("ball")&&x1.getBody().getUserData().equals("ground"))
                {
                    x2.getBody().setActive(false);
                    x2.getBody().setUserData("destroyed");
                }

                if (x2.getBody().getUserData().equals("ball")&&x1.getBody().getUserData().equals("platform"))
                {
                    if (platform.isMagnetActive())
                    {
                        x2.getBody().setUserData("attach");
                    }
                    else
                    {
                        double angle = 0.8 * ((x2.getBody().getPosition().x - x1.getBody().getPosition().x) * 32 / (platform.getWidth() / 2) * 90);
                        x2.getBody().setLinearVelocity(Ball.computeVelocity(180 - angle));
                    }

                }

                if (x2.getBody().getUserData().equals("ball")&&x1.getBody().getUserData().equals("block"))
                {
                    x2.getBody().setLinearVelocity(x2.getBody().getLinearVelocity().x,-(x2.getBody().getLinearVelocity().y));
                    x1.getBody().setUserData("destroyed");
                }



            }

            @Override
            public void endContact(Contact contact)
            {

            }

            @Override
            public void preSolve(Contact contact, Manifold oldManifold)
            {

            }

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse)
            {

            }
        };
        return contactListener;
    }
}
