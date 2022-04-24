package com.andrew.stormytutorial;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class CurrentWeather {

    public CurrentWeather() {
    }

    public CurrentWeather(String locationLabel, String icon, long time, double temperature,
                          double humidity, double precipitationChance, String summary,
                          String timeZone) {
        this.locationLabel = locationLabel;
        this.icon = icon;
        this.time = time;
        this.temperature = temperature;
        this.humidity = humidity;
        this.precipitationChance = precipitationChance;
        this.summary = summary;
        this.timeZone = timeZone;
    }

    private String locationLabel;
    private String icon;
    private long time;
    private double temperature;
    private double humidity;
    private  double precipitationChance;
    private String summary;
    private String timeZone;

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public String getLocationLabel() {
        return locationLabel;
    }

    public void setLocationLabel(String locationLabel) {
        this.locationLabel = locationLabel;
    }

    public String getIcon() {

        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getIconID(){

        //01d, 01n, 02d, 02n few clouds, 03d, 03n scattered clouds, 04d,
        // 04n broken clouds, 09d, 09n shower rain, 10d, 10n rain, 11d, 11n thunderstorm,
        // 13d, 13n snow, 50d, 50 n mist

        int iconID = R.drawable.clear_day;

        switch(icon){
            case "01d":
                iconID = R.drawable.clear_day;
                break;
            case "01n":
                iconID = R.drawable.clear_night;
                break;
            case "02d":
                iconID = R.drawable.partly_cloudy;
                break;
            case "02n":
            case "03n":
            case "04n":
                iconID = R.drawable.cloudy_night;
                break;
            case "03d":
            case "04d":
                iconID = R.drawable.cloudy;
                break;
            case "09d":
            case "09n":
            case "10d":
            case "10n":
            case "11d":
            case "11n":
                iconID = R.drawable.rain;
                break;
            case "13d":
            case "13n":
                iconID = R.drawable.snow;
                break;
            case "50d":
            case "50n":
                iconID = R.drawable.fog;
                break;
        }
        return iconID;
    }

    public long getTime() {
        return time;
    }

    public String getFormattedTime(){
        SimpleDateFormat formatter = new SimpleDateFormat("h:mm a");

        formatter.setTimeZone(TimeZone.getTimeZone(timeZone));

        Date dateTime = new Date(time * 1000);

        return formatter.format(dateTime);
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getTemperature() {
        int temperature = (int) (Math.round(this.temperature));

        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public double getHumidity() {
        return humidity;
    }

    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }

    public int getPrecipitationChance() {
        int precipitationChance = (int) Math.round(this.precipitationChance);
        return precipitationChance;
    }

    public void setPrecipitationChance(double precipitationChance) {
        this.precipitationChance = precipitationChance;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }
}
