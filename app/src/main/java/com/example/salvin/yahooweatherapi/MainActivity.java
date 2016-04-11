package com.example.salvin.yahooweatherapi;

import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.salvin.yahooweatherapi.data.Channel;
import com.example.salvin.yahooweatherapi.data.Condition;

public class MainActivity extends AppCompatActivity implements WeatherServiceListener {

    private ImageView weatherIconImageView;
    private TextView temperatureTextView;
    private TextView conditionTextView;
    private TextView locationTextView;

    private YahooWeatherService weatherService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        weatherIconImageView = (ImageView) findViewById(R.id.weatherIconImageView);
        temperatureTextView = (TextView) findViewById(R.id.temperatureTextView);
        conditionTextView = (TextView) findViewById(R.id.conditionTextView);
        locationTextView = (TextView) findViewById(R.id.locationTextView);
        Spinner dynamicSpinner = (Spinner) findViewById(R.id.spinner);

        weatherService = new YahooWeatherService(this);

        String[] items = new String[] { "Dhaka", "Chittagong", "Barisal", "Sylhet", "Rajshahi", "Khulna", "Rangpur", "Dinajpur", "Sirajganj", "Teknaf", "Mymensingh", "Coxs-bazar"};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items);

        dynamicSpinner.setAdapter(adapter);

        dynamicSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
//                Log.v("item", (String) parent.getItemAtPosition(position));
                weatherService.refreshWeather(parent.getItemAtPosition(position).toString());

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                weatherService.refreshWeather("Dhaka");
            }
        });


    }

    @Override
    public void serviceSuccess(Channel channel) {
        Condition condition = channel.getItem().getCondition();

        int resourceId = getResources().getIdentifier("drawable/icon_" + condition.getCode(), null, getPackageName());

        @SuppressWarnings("deprecation")
        Drawable weatherIconDrawable = getResources().getDrawable(resourceId);

        weatherIconImageView.setImageDrawable(weatherIconDrawable);
        String temperatureLabel = getString(R.string.temperature_output, condition.getTemperature(), channel.getUnits().getTemperature());

        temperatureTextView.setText(temperatureLabel);
        conditionTextView.setText(condition.getDescription());
        locationTextView.setText(channel.getLocation());
    }

    @Override
    public void serviceFailure(Exception exception) {
        Toast.makeText(this, exception.getMessage(), Toast.LENGTH_LONG).show();
    }
}
