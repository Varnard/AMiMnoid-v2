package com.makeinfo.andenginetemplate.Objects;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.makeinfo.andenginetemplate.Game;
import com.makeinfo.andenginetemplate.TextureMap;

import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;

import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import java.util.ArrayList;

public class Platform extends Sprite {

    private float maxSpeed;
    private boolean magnet;
    private float target;
    private Body body;
    float ptm;

    private ArrayList<Ball> attachedBalls;


    public Platform(float pX, float pY, VertexBufferObjectManager pVertexBufferObjectManager, PhysicsWorld physicsWorld)
    {
        super(pX-64,pY, TextureMap.getInstance().get("platform"),pVertexBufferObjectManager);
        ptm=32;
        maxSpeed=10;
        magnet=true;
        target=pX;
        body = PhysicsFactory.createBoxBody(physicsWorld,this, BodyDef.BodyType.KinematicBody, Game.PLATFORM_FIXTURE_DEF);
        body.getPosition().x=pX;
        body.setUserData("platform");
        physicsWorld.registerPhysicsConnector(new PhysicsConnector(this,body,true,false));
        attachedBalls = new ArrayList<>();

    }

    public void setMagnet(boolean magnet) {
        this.magnet = magnet;
    }

    public void setMaxSpeed(float maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public void setTarget(float target) {
        this.target = target;
    }

    public synchronized void attachBall(Ball ball)
    {
        ball.getBody().setUserData("attached");
        ball.getBody().setLinearVelocity(0,0);
        attachedBalls.add((ball));
    }

    public Body getBody() {
        return body;
    }

    public boolean isMagnetActive() {
        return magnet;
    }

    public ArrayList<Ball> getAttachedBalls() {
        return attachedBalls;
    }

    public synchronized void move()
    {
        float difference = body.getPosition().x*ptm-target;
        if (difference>0)
        {
            if (difference>maxSpeed)
            {
                body.setLinearVelocity(-maxSpeed,0);
            }
            else  body.setLinearVelocity(-difference,0);
        }
        else if (difference<0)
        {
            if (Math.abs(difference)>maxSpeed)
            {
                body.setLinearVelocity(maxSpeed,0);
            }
            else  body.setLinearVelocity(-difference,0);
        }
        else body.setLinearVelocity(0,0);

        for (Ball ball : attachedBalls)
        {
            ball.getBody().setLinearVelocity(body.getLinearVelocity());
        }

    }

}
