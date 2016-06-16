package com.makeinfo.andenginetemplate.Games;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.makeinfo.andenginetemplate.Activities.GameActivity;
import com.makeinfo.andenginetemplate.Activities.MenuActivity;
import com.makeinfo.andenginetemplate.GameState;
import com.makeinfo.andenginetemplate.MapLoader;
import com.makeinfo.andenginetemplate.Objects.Ball;
import com.makeinfo.andenginetemplate.Objects.Block;
import com.makeinfo.andenginetemplate.Objects.Booster;
import com.makeinfo.andenginetemplate.Objects.Platform;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.hud.HUD;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import java.util.ArrayList;
import java.util.Random;

public class Game {

    Context context;
    protected Scene scene;
    protected Engine mEngine;
    protected PhysicsWorld physicsWorld;
    protected GameState gameState;

    protected Platform platform;
    protected ArrayList<Ball> balls;
    protected ArrayList<Booster> boosters;
    protected Block[][] blocks;
    protected Body leftWall;
    protected Body rightWall;
    protected Body ceiling;
    protected Body ground;

    /* The categories. */
    public static final short CATEGORYBIT_WALL = 1;
    public static final short CATEGORYBIT_PlATFORM = 2;
    public static final short CATEGORYBIT_BOOSTER = 4;
    public static final short CATEGORYBIT_BALL = 8;

    /* And what should collide with what. */
    public static final short MASKBITS_WALL = CATEGORYBIT_BALL;
    public static final short MASKBITS_PlATFORM = CATEGORYBIT_BALL + CATEGORYBIT_BOOSTER;
    public static final short MASKBITS_BALL = CATEGORYBIT_WALL + CATEGORYBIT_PlATFORM;
    public static final short MASKBITS_BOOSTER = CATEGORYBIT_PlATFORM;

    public static final FixtureDef WALL_FIXTURE_DEF = PhysicsFactory.createFixtureDef(1, 1, 0, false, CATEGORYBIT_WALL, MASKBITS_WALL, (short) 0);
    public static final FixtureDef PLATFORM_FIXTURE_DEF = PhysicsFactory.createFixtureDef(1, 1, 0, false, CATEGORYBIT_PlATFORM, MASKBITS_PlATFORM, (short) 0);
    public static final FixtureDef BALL_FIXTURE_DEF = PhysicsFactory.createFixtureDef(1, 1, 0, false, CATEGORYBIT_BALL, MASKBITS_BALL, (short) 0);
    public static final FixtureDef BOOSTER_FIXTURE_DEF = PhysicsFactory.createFixtureDef(1, 1, 0, false, CATEGORYBIT_BOOSTER, MASKBITS_BOOSTER, (short) 0);


    public Game(String mode, int level, Scene scene, final Engine mEngine, PhysicsWorld physicsWorld, Context context)
    {
        physicsWorld.setContactListener(createContactListener());

        this.scene = scene;
        this.mEngine = mEngine;
        this.physicsWorld = physicsWorld;
        this.gameState = new GameState(mEngine.getCamera().getHUD(), mEngine);
        this.boosters = new ArrayList<>();

        this.context = context;


        final float height = mEngine.getCamera().getHeight();
        final float width = mEngine.getCamera().getWidth();

        VertexBufferObjectManager vboManager = mEngine.getVertexBufferObjectManager();

        createWalls(width, height);

        createPlatform(width, height, vboManager);

        createBalls(width, height, vboManager);

        createBlocks(mode, level, vboManager);

        scene.setOnSceneTouchListener(createTouchListener(width));

    }

    protected void createPlatform(float width, float height, VertexBufferObjectManager vboManager)
    {
        platform = new Platform(width / 2, height - 160, vboManager, physicsWorld);
        scene.attachChild(platform);
    }

    protected void createBalls(float width, float height, VertexBufferObjectManager vboManager)
    {
        balls = new ArrayList<>();

        Ball startBall = new Ball(width / 2, height - 160, 120, vboManager, physicsWorld);
        balls.add(startBall);
        platform.attachBall(startBall);

        for (Ball ball : balls)
        {
            scene.attachChild(ball);
        }
    }

    protected void createWalls(float width, float height)
    {
        leftWall = PhysicsFactory.createBoxBody(physicsWorld, 0, height / 2, 1, height, BodyDef.BodyType.StaticBody, WALL_FIXTURE_DEF);
        leftWall.setUserData("sideWall");
        rightWall = PhysicsFactory.createBoxBody(physicsWorld, width, height / 2, 1, height, BodyDef.BodyType.StaticBody, WALL_FIXTURE_DEF);
        rightWall.setUserData("sideWall");
        ceiling = PhysicsFactory.createBoxBody(physicsWorld, width / 2, 40, width, 1, BodyDef.BodyType.StaticBody, WALL_FIXTURE_DEF);
        ceiling.setUserData("ceiling");
        ground = PhysicsFactory.createBoxBody(physicsWorld, width / 2, height, width, 1, BodyDef.BodyType.StaticBody, WALL_FIXTURE_DEF);
        ground.setUserData("ground");
    }

    protected void createBlocks(String mode, int level, VertexBufferObjectManager vboManager)
    {
        int[][] z = MapLoader.getLevel(mode, level);

        float offset = 42;
        if (mode.equals("mirror")) offset = 202;

        blocks = new Block[16][8];
        float spacingX = (mEngine.getCamera().getWidth() - 64 * 8) / 9;
        float spacingY = 2;
        for (int i = 0; i < 8; i++)
        {
            for (int j = 0; j < 16; j++)
            {
                if (z[j][i] != 0)
                {
                    blocks[j][i] = new Block(i * (64 + spacingX), j * (24 + spacingY) + offset, "block" + z[j][i], vboManager, physicsWorld);
                }
                if (blocks[j][i] != null) scene.attachChild(blocks[j][i]);
            }

        }
    }

    protected void destroyPlatform()
    {
        final Body body = platform.getBody();
        scene.detachChild(platform);
        physicsWorld.unregisterPhysicsConnector(physicsWorld.getPhysicsConnectorManager().findPhysicsConnectorByShape(platform));
        physicsWorld.destroyBody(body);
    }

    protected void destroyBalls()
    {
        for (Ball ball : balls)
        {
            final Body body = ball.getBody();
            scene.detachChild(ball);
            physicsWorld.unregisterPhysicsConnector(physicsWorld.getPhysicsConnectorManager().findPhysicsConnectorByShape(ball));
            physicsWorld.destroyBody(body);
            balls.remove(ball);
        }
    }

    public void update()
    {
        updateObjects();
        if (noBlocksLeft())
        {
            ((GameActivity)context).victory();
        }
        if (noBallsLeft())
        {
            if (gameState.getLives() > 0)
            {
                softReset();
            }
            else
            {
                ((GameActivity)context).gameOver(gameState.getScore());
            }

        }
    }


    public boolean noBallsLeft()
    {
        return balls.isEmpty();
    }

    public boolean noBlocksLeft()
    {
        boolean cleared = true;

        for (int i = 0; i < 8; i++)
        {
            for (int j = 0; j < 16; j++)
            {
                if (blocks[j][i] != null)
                {
                    if (!(blocks[j][i].getBody().getUserData().equals("inv")))cleared = false;
                }
            }
        }
        return cleared;
    }



    public synchronized void softReset()
    {
        if (gameState.getLives() > 0) gameState.removeLife();
        destroyPlatform();
        destroyBalls();

        final float height = mEngine.getCamera().getHeight();
        final float width = mEngine.getCamera().getWidth();

        VertexBufferObjectManager vboManager = mEngine.getVertexBufferObjectManager();

        createPlatform(width, height, vboManager);

        createBalls(width, height, vboManager);
    }

    protected synchronized void updateObjects()
    {
        updateBlocks();

        for (Ball ball : balls)
        {
            updateBall(ball);
        }

        updatePlatform(this.platform);

        for (Booster booster : boosters)
        {
            updateBooster(booster);
        }

    }

    protected void updateBlocks()
    {
        for (int i = 0; i < 8; i++)
        {
            for (int j = 0; j < 16; j++)
            {
                final Block block = blocks[j][i];


                if (block != null)
                {
                    if (block.getBody().getUserData().equals("destroyed"))
                    {
                        gameState.addScore(1000);
                        final Block b = block;
                        final int fj = j;
                        final int fi = i;

                        mEngine.runOnUpdateThread(new Runnable() {
                            @Override
                            public void run()
                            {
                                final Body body = b.getBody();
                                scene.detachChild(b);
                                physicsWorld.unregisterPhysicsConnector(physicsWorld.getPhysicsConnectorManager().findPhysicsConnectorByShape(b));
                                physicsWorld.destroyBody(body);
                                blocks[fj][fi] = null;
                            }
                        });

                        makeBooster(b.getX(), b.getY());
                    }
                }

            }
        }
    }

    protected void updateBall(Ball ball)
    {
        if (ball.getBody().getUserData().equals("destroyed"))
        {
            final PhysicsConnector physicsConnector =
                    physicsWorld.getPhysicsConnectorManager().findPhysicsConnectorByShape(ball);
            final Ball b = ball;

            mEngine.runOnUpdateThread(new Runnable() {
                @Override
                public void run()
                {
                    if (physicsConnector != null)
                    {
                        balls.remove(b);
                        scene.detachChild(b);
                        physicsWorld.unregisterPhysicsConnector(physicsConnector);
                        physicsWorld.destroyBody(b.getBody());
                    }
                }
            });
        }

        if (ball.getBody().getUserData().equals("attach"))
        {
            final Ball b = ball;
            mEngine.runOnUpdateThread(new Runnable() {
                @Override
                public void run()
                {
                    platform.attachBall(b);
                }
            });
        }

        float vY = ball.getBody().getLinearVelocity().y;
        float vX = ball.getBody().getLinearVelocity().x;
        if (vY > 0)
        {
            if (vY < 1) ball.getBody().setLinearVelocity(vX, 1);
        }
        else
        {
            if (vY > -1) ball.getBody().setLinearVelocity(vX, -1);
        }
    }

    protected void updatePlatform(Platform platform)
    {
        platform.move();

        if (platform.getBody().getUserData().equals("launch"))
        {
            platform.getBody().setUserData("platform");
            platform.setMagnet(false);
            final ArrayList<Ball> attachedBalls = platform.getAttachedBalls();
            for (Ball ball : attachedBalls)
            {
                final Ball b = ball;
                final Platform p = platform;
                mEngine.runOnUpdateThread(new Runnable() {
                    @Override
                    public void run()
                    {

                        double angle = 0.6 * ((b.getBody().getPosition().x - p.getBody().getPosition().x) * 32 / (p.getWidth() / 2) * 90);
                        b.getBody().setLinearVelocity(Ball.computeVelocity(180 - angle));
                        attachedBalls.remove(b);
                        b.getBody().setUserData("ball");
                    }
                });
            }
        }
    }

    protected void updateBooster(Booster booster)
    {
        if (booster.getBody().getUserData().equals("destroyed"))
        {
            final PhysicsConnector physicsConnector =
                    physicsWorld.getPhysicsConnectorManager().findPhysicsConnectorByShape(booster);
            final Booster b = booster;

            mEngine.runOnUpdateThread(new Runnable() {
                @Override
                public void run()
                {
                    if (physicsConnector != null)
                    {
                        boosters.remove(b);
                        scene.detachChild(b);
                        physicsWorld.unregisterPhysicsConnector(physicsConnector);
                        physicsWorld.destroyBody(b.getBody());
                    }
                }
            });
        }

        if (booster.getBody().getUserData().equals("activate"))
        {
            final Booster b = booster;
            mEngine.runOnUpdateThread(new Runnable() {
                @Override
                public void run()
                {
                    activateBoost(b.getType());
                    b.getBody().setUserData("destroyed");
                }
            });
        }
    }

    protected void makeBooster(float pX, float pY)
    {
        String type = "none";
        int rng = new Random().nextInt(50);

        if (rng < 4)
        {
            type = "shrink";
        }
        else if (rng < 8)
        {
            type = "expand";
        }
        else if (rng < 12)
        {
            type = "dball";
        }
        else if (rng < 13)
        {
            type = "life";
        }

        if (!type.equals("none"))
        {
            Booster booster = new Booster(pX, pY, type, mEngine.getVertexBufferObjectManager(), physicsWorld);
            boosters.add(booster);
            scene.attachChild(booster);
        }
    }

    protected void activateBoost(String type)
    {
        switch (type)
        {
            case "shrink":
            {
                platform.setScaleX(0.7f);
                platform.recreateBody();
                break;
            }

            case "expand":
            {
                platform.setScaleX(1.3f);
                platform.recreateBody();
                break;
            }

            case "dball":
            {
                ArrayList<Ball> newBalls = new ArrayList<>();
                for (Ball ball : balls)
                {
                    newBalls.add(new Ball(ball.getX(), ball.getY(), 135 + (new Random().nextInt(90)), mEngine.getVertexBufferObjectManager(), physicsWorld));
                }

                for (Ball ball : newBalls)
                {
                    balls.add(ball);
                    scene.attachChild(ball);
                }
                break;
            }

            case "life":
            {
                if (gameState.getLives() < 6) gameState.addLife(mEngine);
                break;
            }
        }
    }

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
                }
                else if (touchX < platform.getWidth() / 2 + 1)
                    platform.setTarget(platform.getWidth() / 2 + 1);
                else platform.setTarget(touchX);

                if (pSceneTouchEvent.isActionUp())
                {
                    platform.getBody().setUserData("launch");
                }

                return false;
            }
        };
    }

    protected ContactListener createContactListener()
    {
        ContactListener contactListener = new ContactListener() {
            @Override
            public synchronized void beginContact(Contact contact)
            {
                final Fixture x1 = contact.getFixtureA();
                final Fixture x2 = contact.getFixtureB();

                if (x2.getBody().getUserData().equals("ball") && x1.getBody().getUserData().equals("sideWall"))
                {
                    x2.getBody().setLinearVelocity(-(x2.getBody().getLinearVelocity().x), x2.getBody().getLinearVelocity().y);
                }


                if (x2.getBody().getUserData().equals("ball") && x1.getBody().getUserData().equals("ceiling"))
                {
                    x2.getBody().setLinearVelocity(x2.getBody().getLinearVelocity().x, -(x2.getBody().getLinearVelocity().y));
                }

                if (x2.getBody().getUserData().equals("ball") && x1.getBody().getUserData().equals("ground"))
                {
                    x2.getBody().setActive(false);
                    x2.getBody().setUserData("destroyed");
                }

                if (x2.getBody().getUserData().equals("ball") && x1.getBody().getUserData().equals("platform"))
                {
                    if (platform.isMagnetActive())
                    {
                        x2.getBody().setUserData("attach");
                    }
                    else
                    {
                        double angle = 0.6 * ((x2.getBody().getPosition().x - x1.getBody().getPosition().x) * 32 / (platform.getWidth() / 2) * 90);
                        x2.getBody().setLinearVelocity(Ball.computeVelocity(180 - angle));
                    }

                }

                if (x2.getBody().getUserData().equals("ball") && x1.getBody().getUserData().equals("block"))
                {
                    float ptm = 32;
                    float diffX = (Math.abs(x2.getBody().getPosition().x - x1.getBody().getPosition().x) * ptm);
                    if (diffX > 35)
                    {
                        x2.getBody().setLinearVelocity(-(x2.getBody().getLinearVelocity().x), x2.getBody().getLinearVelocity().y);
                    }
                    else
                        x2.getBody().setLinearVelocity(x2.getBody().getLinearVelocity().x, -(x2.getBody().getLinearVelocity().y));
                    x1.getBody().setUserData("destroyed");
                }

                if (x2.getBody().getUserData().equals("ball") && x1.getBody().getUserData().equals("inv"))
                {
                    float ptm = 32;
                    float diffX = (Math.abs(x2.getBody().getPosition().x - x1.getBody().getPosition().x) * ptm);
                    if (diffX > 35)
                    {
                        x2.getBody().setLinearVelocity(-(x2.getBody().getLinearVelocity().x), x2.getBody().getLinearVelocity().y);
                    }
                    else
                        x2.getBody().setLinearVelocity(x2.getBody().getLinearVelocity().x, -(x2.getBody().getLinearVelocity().y));
                }

                if (x2.getBody().getUserData().equals("booster") && x1.getBody().getUserData().equals("platform"))
                {
                    x2.getBody().setActive(false);
                    x2.getBody().setUserData("activate");
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
