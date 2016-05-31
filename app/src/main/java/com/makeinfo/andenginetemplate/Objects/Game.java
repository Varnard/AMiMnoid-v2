package com.makeinfo.andenginetemplate.Objects;


import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;

import org.andengine.engine.Engine;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

public class Game {

    private Platform platform;
    private Ball ball;
    private Block[][] blocks;
    private Body leftWall;
    private Body rightWall;
    private Body ceiling;

    private Scene scene;
    private Engine mEngine;

    public Game(Scene scene, final Engine mEngine, PhysicsWorld physicsWorld)
    {
        physicsWorld.setContactListener(createContactListener());

        this.scene=scene;
        this.mEngine=mEngine;

        final FixtureDef fixtureDef = PhysicsFactory.createFixtureDef(1, 1, 0);
        final float height = mEngine.getCamera().getHeight();
        final float width = mEngine.getCamera().getWidth();

        VertexBufferObjectManager vboManager = mEngine.getVertexBufferObjectManager();

        leftWall= PhysicsFactory.createBoxBody(physicsWorld,0,height/2,1,height, BodyDef.BodyType.StaticBody,fixtureDef);
        leftWall.setUserData("leftWall");
        rightWall= PhysicsFactory.createBoxBody(physicsWorld,width,height/2,1,height, BodyDef.BodyType.StaticBody,fixtureDef);
        rightWall.setUserData("rightWall");
        ceiling= PhysicsFactory.createBoxBody(physicsWorld,width/2,0,width,1,BodyDef.BodyType.StaticBody,fixtureDef);
        ceiling.setUserData("ceiling");

        platform = new Platform(width/2,height-160,vboManager,physicsWorld);

        ball = new Ball(270,600,60,vboManager,physicsWorld);

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
                blocks[j][i]=new Block(i*bsX,j*20,bsX-1,bsY-1,vboManager,z[j][i]);
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

                if(touchX>width - 1 - platform.getWidth()/2)
                {
                    platform.setTarget(width- platform.getWidth()/2-1);
                }
                else if(touchX< platform.getWidth()/2+1) platform.setTarget(platform.getWidth()/2+1);
                else platform.setTarget(touchX);

                return false;
            }
        });
    }

    private void checkPlatformCollision(Ball ball)
    {
        if (ball.collidesWith(platform))
        {
            double angle =0.8*(((ball.getX()+ball.getWidth()/2)-(platform.getX()+platform.getWidth()/2))/(platform.getWidth()/2)*90);
            ball.getBody().setLinearVelocity(Ball.computeVelocity(180-angle));
        }
    }


    public void update()
    {
        platform.move();
        checkPlatformCollision(ball);
    }

    private ContactListener createContactListener()
    {
        ContactListener contactListener = new ContactListener()
        {
            @Override
            public void beginContact(Contact contact)
            {
                final Fixture x1 = contact.getFixtureA();
                final Fixture x2 = contact.getFixtureB();

                if (x2.getBody().getUserData().equals("ball")&&x1.getBody().getUserData().equals("leftWall"))
                {
                    x2.getBody().setLinearVelocity(-(x2.getBody().getLinearVelocity().x),x2.getBody().getLinearVelocity().y);
                }

                if (x2.getBody().getUserData().equals("ball")&&x1.getBody().getUserData().equals("rightWall"))
                {
                    x2.getBody().setLinearVelocity(-(x2.getBody().getLinearVelocity().x),x2.getBody().getLinearVelocity().y);
                }

                if (x2.getBody().getUserData().equals("ball")&&x1.getBody().getUserData().equals("ceiling"))
                {
                    x2.getBody().setLinearVelocity(x2.getBody().getLinearVelocity().x,-(x2.getBody().getLinearVelocity().y));
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
