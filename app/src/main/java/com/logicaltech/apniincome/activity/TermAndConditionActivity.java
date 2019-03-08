package com.logicaltech.apniincome.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;

import com.logicaltech.apniincome.R;

public class TermAndConditionActivity extends AppCompatActivity
{
    ImageView imageView_back_arrow;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_and_condition);
        imageView_back_arrow = (ImageView) findViewById(R.id.term_and_condition_back_arrow);
        imageView_back_arrow.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });
     /*   WebView webView = (WebView) findViewById(R.id.webview);
        webView.loadUrl("https://www.apniincome.com/");*/

    }
}
