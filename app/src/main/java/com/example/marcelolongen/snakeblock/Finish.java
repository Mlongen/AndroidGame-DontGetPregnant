package com.example.marcelolongen.snakeblock;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.w3c.dom.Text;

public class Finish extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish);

        Intent intent = getIntent();
        int scoreInt = intent.getIntExtra("score", 0);


        TextView score = findViewById(R.id.scoreText);
        score.setText(scoreInt + "");
    }
}
