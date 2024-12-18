package com.example.frontend;

import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.appcompat.app.AppCompatActivity;

public class TrailerActivity extends AppCompatActivity {

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trailer);

        webView = findViewById(R.id.webview);

        String videoUrl = getIntent().getStringExtra("trailerUrl");
        if (videoUrl != null && !videoUrl.isEmpty()) {
            // Ensure the URL is the full YouTube video URL
            String embedUrl = videoUrl.replace("watch?v=", "embed/");

            // Configure WebView to play YouTube video
            webView.getSettings().setJavaScriptEnabled(true);
            webView.setWebChromeClient(new WebChromeClient());
            webView.setWebViewClient(new WebViewClient());
            webView.loadUrl(embedUrl);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        webView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        webView.onResume();
    }
}