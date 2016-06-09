package com.makeinfo.andenginetemplate.Objects;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.makeinfo.andenginetemplate.Games.Game;
import com.makeinfo.andenginetemplate.TextureMap;

import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.vbo.VertexBufferObjectManager;


public class Ball extends Sprite {

    private static float maxSpeed;
    private Body body;

    public Ball(float pX, float pY, double angle, VertexBufferObjectManager pVertexBufferObjectManager, PhysicsWorld physicsWorld)
    {
        super(pX, pY, TextureMap.getInstance().get("ball"), pVertexBufferObjectManager);
        maxSpeed = 8;
        body = PhysicsFactory.createCircleBody(physicsWorld, this, BodyDef.BodyType.DynamicBody, Game.BALL_FIXTURE_DEF);
        body.setUserData("ball");
        physicsWorld.registerPhysicsConnector(new PhysicsConnector(this, body, true, false));
        body.setLinearVelocity(Ball.computeVelocity(angle));

    }

    public Body getBody()
    {
        return body;
    }

    public static Vector2 computeVelocity(double angle)
    {
        float velX = maxSpeed * (float) Math.sin(Math.toRadians(angle));
        float velY = maxSpeed * (float) Math.cos(Math.toRadians(angle));
        return new Vector2(velX, velY);
    }

    public void setVelocityX(float velX)
    {
        body.setLinearVelocity(velX, body.getLinearVelocity().y);
    }

    public void setVelocityY(float velY)
    {
        body.setLinearVelocity(body.getLinearVelocity().x, velY);
    }

    public void reverseVelocityX()
    {
        body.setLinearVelocity(body.getLinearVelocity().x * -1, body.getLinearVelocity().y);
    }

    public void reverseVelocityY()
    {
        body.setLinearVelocity(body.getLinearVelocity().x, body.getLinearVelocity().y * -1);
    }

}
