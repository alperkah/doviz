package com.hassamyo.doviz;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.DecimalFormat;

public class DolarTlActivity extends AppCompatActivity {

    private EditText dolarEditText;
    private Button donusturButton;
    private TextView tlTextView;
    private TextView rateInfoTextView;
    private ImageView currencyIcon;
    private CardView inputCard;
    private CardView resultCard;
    private ProgressBar loadingProgressBar;
    private CurrencyApiService apiService;
    private double currentRate = 42.0; // Fallback rate

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dolar_tl);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize API service
        apiService = new CurrencyApiService();

        // Initialize views
        dolarEditText = findViewById(R.id.dolarEditText);
        donusturButton = findViewById(R.id.donusturButton);
        tlTextView = findViewById(R.id.tlTextView);
        currencyIcon = findViewById(R.id.currencyIcon);
        inputCard = findViewById(R.id.inputCard);
        resultCard = findViewById(R.id.resultCard);

        // Find rate info TextView from layout
        rateInfoTextView = findViewById(R.id.rateInfoTextView);

        // Load animations
        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        Animation scaleUp = AnimationUtils.loadAnimation(this, R.anim.scale_up);

        // Animate views on start
        currencyIcon.startAnimation(scaleUp);
        inputCard.startAnimation(fadeIn);
        resultCard.startAnimation(scaleUp);

        // Fetch current rate
        fetchCurrentRate();

        donusturButton.setOnClickListener(v -> {
            // Button click animation
            v.animate()
                .scaleX(0.95f)
                .scaleY(0.95f)
                .setDuration(100)
                .withEndAction(() -> {
                    v.animate()
                        .scaleX(1f)
                        .scaleY(1f)
                        .setDuration(100)
                        .withEndAction(this::performConversion);
                });
        });
    }

    private void fetchCurrentRate() {
        rateInfoTextView.setText("Kur bilgisi yükleniyor...");

        apiService.getUsdToTryRate(new CurrencyApiService.CurrencyCallback() {
            @Override
            public void onSuccess(double rate) {
                currentRate = rate;
                DecimalFormat df = new DecimalFormat("#,##0.00");
                rateInfoTextView.setText("Güncel kur: 1$ = " + df.format(rate) + "₺");
            }

            @Override
            public void onError(String error) {
                rateInfoTextView.setText("Güncel kur: 1$ = " + currentRate + "₺ (Varsayılan)");
                Toast.makeText(DolarTlActivity.this,
                    "Kur güncellenemedi, varsayılan kur kullanılıyor",
                    Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void performConversion() {
        String girilenDolar = dolarEditText.getText().toString().trim();

        if (TextUtils.isEmpty(girilenDolar)) {
            Toast.makeText(this, "Lütfen bir miktar giriniz", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            double girilenDolarDouble = Double.parseDouble(girilenDolar);
            double donusturulmusTl = girilenDolarDouble * currentRate;

            DecimalFormat df = new DecimalFormat("#,##0.00");
            tlTextView.setText(df.format(donusturulmusTl) + " ₺");

            // Animate result
            resultCard.animate()
                .scaleX(1.05f)
                .scaleY(1.05f)
                .setDuration(200)
                .withEndAction(() -> {
                    resultCard.animate()
                        .scaleX(1f)
                        .scaleY(1f)
                        .setDuration(200);
                });

        } catch (NumberFormatException e) {
            Toast.makeText(this, "Geçersiz sayı formatı", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (apiService != null) {
            apiService.shutdown();
        }
    }
}
