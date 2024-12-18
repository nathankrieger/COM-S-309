package com.example.androidexample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class NewActivity extends AppCompatActivity {

    private TextView basicTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new);

        /* initialize UI elements */
        basicTxt = findViewById(R.id.simple_text);
        /* when increase btn is pressed, counter++, reset number textview */

        /* when decrease btn is pressed, counter--, reset number textview */



    }
}