package ro.mta.secondapplication;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import ro.mta.secondapplication.models.DummyForecastGenerator;

public class ChartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);

        ChartView chartView = findViewById(R.id.chartView);
        chartView.temperatures = DummyForecastGenerator.getForecast();
    }
}
