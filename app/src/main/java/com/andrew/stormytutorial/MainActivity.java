package com.andrew.stormytutorial;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

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

    private CurrentWeather currentWeather;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
    double latitude = 37.8267 ;
    double longitude = -122.4233;
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
                System.out.println("POOP");
            }

            @Override public void onResponse(Call call, Response response) throws IOException {
                {
                    try {
                        String jsonData = response.body().string();
                        Log.v(TAG, jsonData);
                        if (response.isSuccessful()){
                            currentWeather = getCurrentDetails(jsonData);
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

        Log.i(TAG, currentWeather.getFormattedTime());
        Log.i(TAG, "From JSON: " + temp);

        return currentWeather;
    }

    private void alertUserAboutError() {
        AlertDialogFragment dialog = new AlertDialogFragment();
        dialog.show(getFragmentManager(), "error_dialog");
    }


}