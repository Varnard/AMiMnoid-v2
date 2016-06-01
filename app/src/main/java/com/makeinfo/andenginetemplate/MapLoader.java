package com.makeinfo.andenginetemplate;


import android.content.Context;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

public class MapLoader
{
    private static Context context;

    public static void setContext(Context context) {
        MapLoader.context = context;
    }

    public static int[][] getLevel(String mode, int level)
    {
        int[][] levelMap = new int[16][8];

        InputStream is = new InputStream()
        {
            @Override
            public int read() throws IOException {
                return 0;
            }
        };

        BufferedInputStream buf = new BufferedInputStream(is);

            is = context.getResources().openRawResource(R.raw.classic1);
            buf = new BufferedInputStream(is);

        if (is != null)
        {
            int i = 0;
            int column;
            int row;
            int tmp;
            try {
                while ((tmp = buf.read()) != -1) {
                    column=i%8;
                    row=i/8;
                    switch (tmp)
                    {
                        case 48:
                        {
                            levelMap[row][column] = 0;
                            i++;
                            break;
                        }
                        case 49:
                        {
                            levelMap[row][column] = 1;
                            i++;
                            break;
                        }

                        case 50:
                        {
                            levelMap[row][column] = 2;
                            i++;
                            break;
                        }
                        case 51:
                        {
                            levelMap[row][column] = 3;
                            i++;
                            break;
                        }

                    }
                }
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return levelMap;
    }
}
