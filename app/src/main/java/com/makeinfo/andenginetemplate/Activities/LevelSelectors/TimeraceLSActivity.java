

package com.makeinfo.andenginetemplate.Activities.LevelSelectors;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.makeinfo.andenginetemplate.Activities.GameActivity;
import com.makeinfo.andenginetemplate.R;

public class TimeraceLSActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_timerace_ls);
    }

    public void start(View view)
    {
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra("mode","timerace");
        startActivity(intent);
    }
}
