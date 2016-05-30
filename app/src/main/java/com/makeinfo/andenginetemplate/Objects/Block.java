package com.makeinfo.andenginetemplate.Objects;

import android.graphics.Canvas;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;

import org.andengine.entity.primitive.Rectangle;
import org.andengine.opengl.vbo.VertexBufferObjectManager;


public class Block extends Rectangle{

    public Block(float pX, float pY, float pWidth, float pHeight,
                 VertexBufferObjectManager pVertexByfferObjectManager, int type)
    {
        super(pX,pY,pWidth,pHeight,pVertexByfferObjectManager);
        switch (type)
        {
            case 1:
            {
                this.setColor(1f,0.2f,0.2f);
                break;
            }
            case 2:
            {
                this.setColor(1f,1f,0.2f);
                break;
            }

            case 3:
            {
                this.setColor(0.2f,1f,0.2f);
                break;
            }

        }
    }

}
