package com.example.android.explicitintent;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class ChildActivity extends AppCompatActivity {

    private TextView textView;
    private String data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child);

        textView = findViewById(R.id.tv_display);

        Intent  intent = getIntent();
        if(intent.hasExtra(Intent.EXTRA_TEXT))
        {
            data =  intent.getStringExtra(Intent.EXTRA_TEXT);
            textView.setText(data);
        }

    }
}