package com.makeinfo.andenginetemplate.Objects;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.makeinfo.andenginetemplate.Games.Game;
import com.makeinfo.andenginetemplate.TextureMap;

import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

public class Booster extends Sprite {

    private Body body;
    private String type;

    public Booster(float pX, float pY, String type, VertexBufferObjectManager pVertexByfferObjectManager, PhysicsWorld physicsWorld)
    {
        super(pX, pY, TextureMap.getInstance().get(type), pVertexByfferObjectManager);
        this.type=type;
        body = PhysicsFactory.createBoxBody(physicsWorld, this, BodyDef.BodyType.DynamicBody, Game.BOOSTER_FIXTURE_DEF);
        body.getPosition().x = pX;
        body.setUserData("booster");
        physicsWorld.registerPhysicsConnector(new PhysicsConnector(this, body, true, false));
        body.setLinearVelocity(0,2);
    }

    public Body getBody()
    {
        return body;
    }

    public String getType()
    {
        return type;
    }
}
