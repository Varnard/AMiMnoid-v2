package com.makeinfo.andenginetemplate;


import android.content.Context;
import android.content.res.AssetManager;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class Highscores {

    private static Context context;

    public static void setContext(Context context)
    {
        Highscores.context = context;
    }

    private static String[] scores = new String[10];

    public static void loadHighscores()
    {
        try
        {
            InputStream is = context.openFileInput("highscores");

            InputStreamReader inputStreamReader = new InputStreamReader(is);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            for (int i = 0; i < 10; i++)
            {
                scores[i]=bufferedReader.readLine();
            }
            is.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static String[] getScores()
    {
        return scores;
    }

    public static void updateScores(Integer newScore)
    {
        try
        {
            for (int i = 0; i < 10; i++)
            {
                if (newScore > Integer.parseInt(scores[i]))
                {
                    scores[i] = newScore.toString();
                    break;
                }
            }

            FileOutputStream fos = context.openFileOutput("highscores", Context.MODE_PRIVATE);
            for (int i = 0; i < 10; i++)
            {
                fos.write(scores[i].getBytes());
            }
            fos.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
