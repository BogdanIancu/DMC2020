package ro.mta.secondapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import java.util.List;

import ro.mta.secondapplication.adapters.CustomForecastAdapter;
import ro.mta.secondapplication.models.DummyForecastGenerator;
import ro.mta.secondapplication.models.WeatherForecast;
import ro.mta.secondapplication.workers.ForecastWorker;

public class ListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        ListView listView = findViewById(R.id.listView);

        Intent intent = getIntent();
        if(intent.hasExtra("location")) {
            String location = intent.getStringExtra("location");
            ForecastWorker worker = new ForecastWorker(listView, getApplicationContext());
            worker.execute(location);
        }
    }
}
