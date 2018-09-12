package com.example.marcelolongen.DontGetPregnant;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

public class Start extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
//Remove notification bar

        setContentView(R.layout.activity_start);
        this.getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.mytitle);
        TextView text = findViewById(R.id.myTitle);
        text.setText("Welcome!");

    }

    public void startGame(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
