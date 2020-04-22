package ro.mta.secondapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import ro.mta.secondapplication.models.PollutionMeasurement;

public class DashboardActivity extends AppCompatActivity {
    private List<String> locationsList = new ArrayList<>();
    private Spinner locationsSpinner;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        Button detailsButton = findViewById(R.id.detailsButton);
        detailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://aerlive.ro"));
                startActivity(intent);
            }
        });

        Button addPollutionButton = findViewById(R.id.addPollutionButton);
        addPollutionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =
                        new Intent(DashboardActivity.this,
                        MainActivity.class);
                intent.putExtra("location", "Bucharest");
                startActivityForResult(intent, 100);
            }
        });

        locationsSpinner = findViewById(R.id.locationSpinner);
        adapter = new ArrayAdapter<>(getApplicationContext(),
               R.layout.support_simple_spinner_dropdown_item, locationsList);
        locationsSpinner.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 100 && resultCode == RESULT_OK) {
            PollutionMeasurement measurement =
                    (PollutionMeasurement) data.getSerializableExtra("measurement");
            TextView pollutionTextView = findViewById(R.id.pollutionTextView);
            Integer value = measurement.getValue();
            pollutionTextView.setText(value.toString());
            locationsList.add(measurement.getArea());
            adapter.notifyDataSetChanged();
        }
    }

    public void navigateToForecastList(View view) {
        Intent intent = new Intent(DashboardActivity.this, ListActivity.class);
        startActivity(intent);
    }
}
