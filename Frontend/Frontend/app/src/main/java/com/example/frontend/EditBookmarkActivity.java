package com.example.frontend;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


public class EditBookmarkActivity extends AppCompatActivity {

    private EditText bookmarkNameEditText, movieIdEditText;
    private Button saveEditButton, cancelButton;
    private int bookmarkId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_bookmark);

        bookmarkNameEditText = findViewById(R.id.bookmarkName);
        movieIdEditText = findViewById(R.id.movieId);
        saveEditButton = findViewById(R.id.saveBookmarkButton);
        cancelButton = findViewById(R.id.cancelButton);

        // Retrieve the passed bookmark details
        bookmarkId = getIntent().getIntExtra("bookmarkId", -1);
        String bookmarkName = getIntent().getStringExtra("bookmarkName");
        int movieId = getIntent().getIntExtra("movieId", -1);

        // Populate the fields with the current bookmark details
        bookmarkNameEditText.setText(bookmarkName);
        movieIdEditText.setText(String.valueOf(movieId));

        saveEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String updatedName = bookmarkNameEditText.getText().toString();
                String updatedMovieIdStr = movieIdEditText.getText().toString();

                if (!updatedName.isEmpty() && !updatedMovieIdStr.isEmpty()) {
                    int updatedMovieId = Integer.parseInt(updatedMovieIdStr);

                    // Send the updated bookmark data back to BookmarksActivity
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("bookmarkId", bookmarkId);
                    resultIntent.putExtra("updatedBookmarkName", updatedName);
                    resultIntent.putExtra("updatedMovieId", updatedMovieId);
                    setResult(RESULT_OK, resultIntent);
                    finish(); // Close the EditBookmarkActivity
                } else {
                    Toast.makeText(EditBookmarkActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                }
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Cancel and return to the bookmarks list without saving
                finish();
            }
        });
    }
}
