package com.makeinfo.andenginetemplate;


import org.andengine.opengl.font.Font;
import org.andengine.opengl.texture.region.TextureRegion;

import java.util.HashMap;

public class FontMap {
    private static HashMap<String, Font> fonts;

    public static HashMap<String, Font> getInstance()
    {
        if (fonts == null)
        {
            fonts = new HashMap<>();
        }
        return fonts;
    }
}
