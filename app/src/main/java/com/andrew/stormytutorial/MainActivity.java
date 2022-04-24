package com.andrew.stormytutorial;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.andrew.stormytutorial.databinding.ActivityMainBinding;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.*;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();

    private ActivityMainBinding binding;
    private CurrentWeather currentWeather;
    private ImageView iconImageView;

    private double latitude = 37.8267 ;
    private double longitude = -122.4233;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getForecast();
    }

    private void getForecast(){

        binding = DataBindingUtil.setContentView(MainActivity.this,
                R.layout.activity_main);

        TextView attributeText = findViewById(R.id.attribution);
        attributeText.setMovementMethod(LinkMovementMethod.getInstance());
        iconImageView = findViewById(R.id.iconImageView);


            if (isNetworkAvailable()) {
                try {
                    run();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();

        boolean isAvailable = false;

        if (networkInfo != null && networkInfo.isConnected()){
            isAvailable = true;
        }
        else{
            Toast.makeText(this, R.string.network_unavailable_message,
                    Toast.LENGTH_LONG).show();
        }

        return isAvailable;
    }

    String apiKey = "3ab49fc3c2b6e93bf90275c0df750f11";
    String forecastUrl = "https://api.openweathermap.org/data/2.5/onecall?lat=" + latitude +
            "&lon=" + longitude + "&appid=" + apiKey + "&units=imperial";




    private final OkHttpClient client = new OkHttpClient();

    public void run() throws Exception {
        Request request = new Request.Builder()
                .url(forecastUrl)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override public void onResponse(Call call, Response response) throws IOException {
                {
                    try {
                        String jsonData = response.body().string();
                        Log.v(TAG, jsonData);
                        if (response.isSuccessful()){
                            currentWeather = getCurrentDetails(jsonData);

                            CurrentWeather displayWeather = new CurrentWeather(
                                    currentWeather.getLocationLabel(),
                                    currentWeather.getIcon(),
                                    currentWeather.getTime(),
                                    currentWeather.getTemperature(),
                                    currentWeather.getHumidity(),
                                    currentWeather.getPrecipitationChance(),
                                    currentWeather.getSummary(),
                                    currentWeather.getTimeZone()
                            );
                            binding.setWeather(displayWeather);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Drawable drawable = getResources().getDrawable(displayWeather.getIconID());
                                    iconImageView.setImageDrawable(drawable);
                                }
                            });


                        }
                        else{
                            alertUserAboutError();
                        }
                    } catch (IOException e) {
                        Log.e(TAG, "IO Exception caught: ", e);
                    }
                    catch (JSONException e){
                        Log.e(TAG, "JSON Exception caught: ", e);
                    }
                }
            }
        });
    }

    private CurrentWeather getCurrentDetails(String jsonData) throws JSONException {
        JSONObject forecast = new JSONObject(jsonData);

        String timezone = forecast.getString("timezone");
        Log.i(TAG, "From JSON: " + timezone);

        JSONObject current = forecast.getJSONObject("current");
        double temp = current.getDouble("temp");

        CurrentWeather currentWeather = new CurrentWeather();

        currentWeather.setHumidity(current.getDouble("humidity"));
        currentWeather.setTemperature(current.getDouble("temp"));
        currentWeather.setTime(current.getLong("dt"));
        currentWeather.setIcon(current.getJSONArray("weather")
                .getJSONObject(0).getString("icon"));
        currentWeather.setLocationLabel("Alcatraz Island, CA");
        currentWeather.setPrecipitationChance(forecast.getJSONArray("hourly")
                .getJSONObject(0).getDouble("pop"));
        currentWeather.setTimeZone(timezone);
        currentWeather.setSummary(current.getJSONArray("weather")
                .getJSONObject(0).getString("description"));

        Log.i(TAG, currentWeather.getFormattedTime());
        Log.i(TAG, "From JSON: " + temp);

        return currentWeather;
    }

    private void alertUserAboutError() {
        AlertDialogFragment dialog = new AlertDialogFragment();
        dialog.show(getFragmentManager(), "error_dialog");
    }

    public void refreshOnClick(View view){
        Toast.makeText(this, "Refreshing Data", Toast.LENGTH_LONG).show();
        getForecast();
    }

}