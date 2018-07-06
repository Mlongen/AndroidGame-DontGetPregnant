package com.example.marcelolongen.snakeblock;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private AnimationHelper mAnimationHelper;
    private MainCanvas mRainDrops;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRainDrops = findViewById(R.id.rainDrops);
        mAnimationHelper = new AnimationHelper(mRainDrops, 60);

    }

    public void run(View view) {
        mAnimationHelper.start();
    }

    public void pause(View view) {
        mAnimationHelper.stop();
    }
}
