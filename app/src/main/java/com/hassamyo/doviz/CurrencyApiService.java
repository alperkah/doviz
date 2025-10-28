package com.hassamyo.doviz;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CurrencyApiService {

    private static final String TAG = "CurrencyApiService";
    private static final String API_URL = "https://api.exchangerate-api.com/v4/latest/USD";
    private final ExecutorService executorService;
    private final Handler mainHandler;

    public interface CurrencyCallback {
        void onSuccess(double rate);
        void onError(String error);
    }

    public CurrencyApiService() {
        executorService = Executors.newSingleThreadExecutor();
        mainHandler = new Handler(Looper.getMainLooper());
    }

    public void getUsdToTryRate(CurrencyCallback callback) {
        executorService.execute(() -> {
            try {
                URL url = new URL(API_URL);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(10000);
                connection.setReadTimeout(10000);

                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(connection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    reader.close();

                    JSONObject jsonResponse = new JSONObject(response.toString());
                    JSONObject rates = jsonResponse.getJSONObject("rates");
                    double tryRate = rates.getDouble("TRY");

                    Log.d(TAG, "USD to TRY rate: " + tryRate);

                    mainHandler.post(() -> callback.onSuccess(tryRate));
                } else {
                    mainHandler.post(() -> callback.onError("HTTP Error: " + responseCode));
                }

                connection.disconnect();
            } catch (Exception e) {
                Log.e(TAG, "Error fetching currency rate", e);
                mainHandler.post(() -> callback.onError("Ağ hatası: " + e.getMessage()));
            }
        });
    }

    public void shutdown() {
        executorService.shutdown();
    }
}
