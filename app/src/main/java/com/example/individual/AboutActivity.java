package com.example.individual;

import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        TextView tvGithub = findViewById(R.id.tvGithubLink);
        Button btnBack = findViewById(R.id.btnAboutBack);

        // Enable HTML hyperlink clicking
        tvGithub.setMovementMethod(LinkMovementMethod.getInstance());

        btnBack.setOnClickListener(v -> finish());
    }
}