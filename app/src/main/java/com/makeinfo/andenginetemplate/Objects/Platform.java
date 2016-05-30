package com.makeinfo.andenginetemplate.Objects;

import android.graphics.Canvas;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;

import org.andengine.entity.primitive.Rectangle;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

public class Platform extends Rectangle {

    private float maxSpeed;
    private boolean magnet;
    private float target;

    public Platform(float pX, float pY, float pWidth, float pHeight, VertexBufferObjectManager pVertexByfferObjectManager)
    {
        super(pX,pY,pWidth,pHeight,pVertexByfferObjectManager);
        maxSpeed=4;
        magnet=false;
        target=pX;
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

    public void move()
    {
        float difference = this.getX()-target;
        if (difference>0)
        {
            if (difference>maxSpeed)
            {
                this.setX(getX()-maxSpeed);
            }
            else  this.setX(getX()-difference);
        }
        else if (difference<0)
        {
            if (difference<maxSpeed)
            {
                this.setX(getX()+maxSpeed);
            }
            else  this.setX(getX()+difference);
        }

    }

}
