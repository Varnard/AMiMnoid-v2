package com.makeinfo.andenginetemplate.Objects;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.makeinfo.andenginetemplate.TextureMap;

import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;

import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

public class Platform extends Sprite {

    private float maxSpeed;
    private boolean magnet;
    private float target;
    private final FixtureDef objectFixtureDef = PhysicsFactory.createFixtureDef(1, 1, 0);
    private Body body;
    float ptm;


    public Platform(float pX, float pY, VertexBufferObjectManager pVertexBufferObjectManager, PhysicsWorld physicsWorld)
    {
        super(pX,pY, TextureMap.getInstance().get("platform"),pVertexBufferObjectManager);
        ptm=32;
        maxSpeed=8;
        magnet=false;
        target=pX;
        body = PhysicsFactory.createBoxBody(physicsWorld,this, BodyDef.BodyType.DynamicBody, objectFixtureDef);
        body.getPosition().x=pX;
        body.setUserData("platform");
        physicsWorld.registerPhysicsConnector(new PhysicsConnector(this,body,true,false));
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

    public Body getBody() {
        return body;
    }

    public void move()
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

    }

}
