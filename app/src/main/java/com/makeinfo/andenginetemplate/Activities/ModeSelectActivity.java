package com.makeinfo.andenginetemplate.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.makeinfo.andenginetemplate.Activities.LevelSelectors.ClassicLSActivity;
import com.makeinfo.andenginetemplate.Activities.LevelSelectors.MirrorLSActivity;
import com.makeinfo.andenginetemplate.Activities.LevelSelectors.SnakeLSActivity;
import com.makeinfo.andenginetemplate.Activities.LevelSelectors.TimeraceLSActivity;
import com.makeinfo.andenginetemplate.R;

    public class ModeSelectActivity extends Activity {

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            requestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);

            setContentView(R.layout.activity_mode_select);
        }

        public void classic(View view)
        {
            Intent intent = new Intent(this, ClassicLSActivity.class);
            startActivity(intent);
        }

        public void mirror(View view)
        {
            Intent intent = new Intent(this, MirrorLSActivity.class);
            startActivity(intent);
        }

        public void timerace(View view)
        {
            Intent intent = new Intent(this, TimeraceLSActivity.class);
            startActivity(intent);
        }

        public void snake(View view)
        {
            Intent intent = new Intent(this, SnakeLSActivity.class);
            startActivity(intent);
        }
}
