package com.example.marcelolongen.DontGetPregnant;

import android.content.ComponentName;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.widget.TextView;

public  class MainActivity extends AppCompatActivity {
    private static int score = 0;
    private static Context mContext;
    private static TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);


//Remove notification bar

        setContentView(R.layout.activity_main);
        mContext = this;
//
        this.getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.mytitle);
        text = findViewById(R.id.myTitle);

    }

    public static void updateScore(String helperScore){
        text.setText(helperScore);
    }

}
