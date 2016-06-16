package com.makeinfo.andenginetemplate.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.makeinfo.andenginetemplate.Highscores;
import com.makeinfo.andenginetemplate.R;


public class MenuActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_menu);
        Highscores.setContext(this);
    }

    public void start(View view)
    {
        Intent intent = new Intent(this, ModeSelectActivity.class);
        startActivity(intent);
    }

    public void resume(View view)
    {
        Intent intent = new Intent(this, GameActivity.class);
        startActivity(intent);
    }

    public void configureOptions(View view)
    {

    }

    public void checkHighscores(View view)
    {
        Intent intent = new Intent(this, HighscoreActivity.class);
        startActivity(intent);
    }




}
