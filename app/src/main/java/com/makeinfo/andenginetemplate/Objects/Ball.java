package com.makeinfo.andenginetemplate.Objects;

import android.graphics.Canvas;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.makeinfo.andenginetemplate.TextureMap;

import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.vbo.VertexBufferObject;
import org.andengine.opengl.vbo.VertexBufferObjectManager;


public class Ball extends Sprite {

    private float maxSpeed;
    private float speedX;
    private float speedY;

    public Ball(float pX, float pY, VertexBufferObjectManager pVertexBufferObjectManager)
    {
        super(pX,pY, TextureMap.getInstance().get("ball"),pVertexBufferObjectManager);
        maxSpeed=4;
        speedX= (float)Math.sin(340)*maxSpeed;
        speedY= (float)Math.cos(340)*maxSpeed;
    }

    public void setSpeed(double angle)
    {
        speedX=maxSpeed*(float)Math.sin(angle);
        speedY=maxSpeed*(float)Math.cos(angle);
    }

    public void setSpeedX(float speedX) {
        this.speedX = speedX;
    }

    public void setSpeedY(float speedY) {
        this.speedY = speedY;
    }

    public void reverseSpeedX()
    {
        speedX*=-1;
    }

    public void reverseSpeedY()
    {
        speedY*=-1;
    }


    public void move()
    {
        this.setX(this.getX()+speedX);
        this.setY(this.getY()+speedY);
    }

}
