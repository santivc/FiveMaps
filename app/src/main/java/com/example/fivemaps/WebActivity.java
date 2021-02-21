package com.example.fivemaps;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebActivity extends AppCompatActivity {

    private WebView webView;
    private String url;
    private ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        webView = findViewById(R.id.webview);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null)
            url = bundle.getString("URL");

        loadWebview();

    }

    private void loadWebview() {
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(url);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();//ir a la actividad anterior
        return super.onSupportNavigateUp();
    }

}