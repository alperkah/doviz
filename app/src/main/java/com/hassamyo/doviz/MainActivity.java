package com.hassamyo.doviz;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    Button tlDolarButton;
    Button dolarTLButton;
    ImageView logoIcon;
    TextView baslikTextView;
    TextView subtitleTextView;
    CardView cardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize views
        tlDolarButton = findViewById(R.id.tlDolarButton);
        dolarTLButton = findViewById(R.id.dolarTLButton);
        logoIcon = findViewById(R.id.logoIcon);
        baslikTextView = findViewById(R.id.baslikTextView);
        subtitleTextView = findViewById(R.id.subtitleTextView);
        cardView = findViewById(R.id.cardView);

        // Load animations
        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        Animation scaleUp = AnimationUtils.loadAnimation(this, R.anim.scale_up);

        // Animate views on start
        logoIcon.startAnimation(scaleUp);
        baslikTextView.startAnimation(fadeIn);
        subtitleTextView.startAnimation(fadeIn);
        cardView.startAnimation(scaleUp);

        // Add button click animations
        tlDolarButton.setOnClickListener(v -> {
            v.animate()
                .scaleX(0.95f)
                .scaleY(0.95f)
                .setDuration(100)
                .withEndAction(() -> {
                    v.animate()
                        .scaleX(1f)
                        .scaleY(1f)
                        .setDuration(100)
                        .withEndAction(() -> {
                            Intent intent = new Intent(this, TlDolarActivity.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        });
                });
        });

        dolarTLButton.setOnClickListener(v -> {
            v.animate()
                .scaleX(0.95f)
                .scaleY(0.95f)
                .setDuration(100)
                .withEndAction(() -> {
                    v.animate()
                        .scaleX(1f)
                        .scaleY(1f)
                        .setDuration(100)
                        .withEndAction(() -> {
                            Intent intent = new Intent(this, DolarTlActivity.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        });
                });
        });
    }
}