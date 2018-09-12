package com.example.marcelolongen.DontGetPregnant;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

public class Finish extends AppCompatActivity {
    public static final String MY_PREFERENCES = "myPrefs";
    private int thisScore;
    private int restoredScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setTitle("");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish);

        Intent intent = getIntent();
        thisScore = intent.getIntExtra("score", 0);

        AnimationHelper.stop();
        SharedPreferences prefs = getSharedPreferences(MY_PREFERENCES, MODE_PRIVATE);
        restoredScore = prefs.getInt("highScore", 0);
        if (thisScore > restoredScore) {
            SharedPreferences.Editor editor = getSharedPreferences(MY_PREFERENCES, MODE_PRIVATE).edit();
            editor.putInt("highScore", thisScore);
            editor.apply();
            restoredScore = thisScore;

        }




        TextView score = findViewById(R.id.scoreText);
        score.setText(thisScore + "");
        TextView highScore = findViewById(R.id.highScoreText);
        highScore.setText(restoredScore + "");
    }

    public void restartGame(View view) {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);

    }
}
