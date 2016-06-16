package com.makeinfo.andenginetemplate.Activities;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.makeinfo.andenginetemplate.Highscores;
import com.makeinfo.andenginetemplate.R;

public class HighscoreActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highscore);

        Highscores.loadHighscores();
        String[] scores = Highscores.getScores();

        TextView t1= (TextView)findViewById(R.id.textView10);
        t1.setText(scores[0]);

        TextView t2= (TextView)findViewById(R.id.textView11);
        t2.setText(scores[1]);

        TextView t3= (TextView)findViewById(R.id.textView6);
        t3.setText(scores[2]);

        TextView t4= (TextView)findViewById(R.id.textView5);
        t4.setText(scores[3]);

        TextView t5= (TextView)findViewById(R.id.textView7);
        t5.setText(scores[4]);

        TextView t6= (TextView)findViewById(R.id.textView8);
        t6.setText(scores[5]);

        TextView t7= (TextView)findViewById(R.id.textView12);
        t7.setText(scores[6]);

        TextView t8= (TextView)findViewById(R.id.textView13);
        t8.setText(scores[7]);

        TextView t9= (TextView)findViewById(R.id.textView4);
        t9.setText(scores[8]);

        TextView t10= (TextView)findViewById(R.id.textView9);
        t10.setText(scores[9]);


    }
}
