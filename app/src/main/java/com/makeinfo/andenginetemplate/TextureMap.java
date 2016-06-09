package com.makeinfo.andenginetemplate;


import org.andengine.opengl.texture.region.TextureRegion;

import java.util.HashMap;

public class TextureMap {
    private static HashMap<String, TextureRegion> textures;

    public static HashMap<String, TextureRegion> getInstance()
    {
        if (textures == null)
        {
            textures = new HashMap<>();
        }
        return textures;
    }
}
