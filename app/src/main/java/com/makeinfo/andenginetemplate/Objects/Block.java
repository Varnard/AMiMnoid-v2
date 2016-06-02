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


public class Block extends Sprite {

        private Body body;

    public Block(float pX, float pY, String type,
                 VertexBufferObjectManager pVertexByfferObjectManager, PhysicsWorld physicsWorld)
    {
        super(pX,pY, TextureMap.getInstance().get(type),pVertexByfferObjectManager);
        body = PhysicsFactory.createBoxBody(physicsWorld,this, BodyDef.BodyType.KinematicBody, Game.BLOCK_FIXTURE_DEF);
        body.getPosition().x=pX;
        body.setUserData("block");
        physicsWorld.registerPhysicsConnector(new PhysicsConnector(this,body,true,false));
    }

    public Body getBody() {
        return body;
    }
}
