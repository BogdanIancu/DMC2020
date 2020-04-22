package ro.mta.secondapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import java.util.List;

import ro.mta.secondapplication.adapters.CustomForecastAdapter;
import ro.mta.secondapplication.models.DummyForecastGenerator;
import ro.mta.secondapplication.models.WeatherForecast;

public class ListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        ListView listView = findViewById(R.id.listView);
        List<WeatherForecast> list = DummyForecastGenerator.getForecast();
        CustomForecastAdapter adapter =
                new CustomForecastAdapter(getApplicationContext(),
                        R.layout.forecast_item_layout, list);
        listView.setAdapter(adapter);
    }
}
