package com.hassamyo.doviz;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class TlDolarActivity extends AppCompatActivity {

    private EditText tlEditText;
    private Button donusturButton;
    private TextView dolarTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tl_dolar);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        tlEditText = findViewById(R.id.tlEditText);
        donusturButton = findViewById(R.id.donusturButton);
        dolarTextView = findViewById(R.id.dolarTextView);


        donusturButton.setOnClickListener(v -> {
            String girilenTl = tlEditText.getText().toString();
            int girilenTLInteger = Integer.parseInt(girilenTl);
            int dolarAnlikDegeri = 42;

            int donusturulmusDolar = girilenTLInteger / dolarAnlikDegeri;

            dolarTextView.setText(Integer.toString(donusturulmusDolar) + " Dolar");


        });


    }







}