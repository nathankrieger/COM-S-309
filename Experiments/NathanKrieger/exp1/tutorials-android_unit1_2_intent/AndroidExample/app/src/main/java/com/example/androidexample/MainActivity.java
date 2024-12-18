package com.example.androidexample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView initialText;     // define message textview variable

    private TextView newText;     // define message textview variable
    private Button counterButton;     // define counter button variable

    private Button newPageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);             // link to Main activity XML

        /* initialize UI elements */
        initialText = findViewById(R.id.main_msg_txt);      // link to message textview in the Main activity XML

        // New message on home screen for demo purposes
        newText = findViewById(R.id.main_msg_txt2);      // link to message textview in the Main activity XML
        counterButton = findViewById(R.id.main_counter_btn);// link to counter button in the Main activity XML

        newPageButton = findViewById(R.id.new_btn);

        /* extract data passed into this activity from another activity */
        Bundle extras = getIntent().getExtras();
        if(extras == null) {
            initialText.setText("Nathan Krieger Edit"); // text change
        } else {
            String number = extras.getString("NUM");  // this will come from LoginActivity
            initialText.setText("The number was " + number + " EDIT"); // Changed message text is extras is not null
        }

        /* click listener on counter button pressed */
        counterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /* when counter button is pressed, use intent to switch to Counter Activity */
                Intent intent = new Intent(MainActivity.this, CounterActivity.class);
                startActivity(intent);
            }
        });

        newPageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /* when counter button is pressed, use intent to switch to Counter Activity */
                Intent intent = new Intent(MainActivity.this, NewActivity.class);
                startActivity(intent);
            }
        });
    }

}