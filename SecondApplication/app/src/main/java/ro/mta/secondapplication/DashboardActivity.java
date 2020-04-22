package ro.mta.secondapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import ro.mta.secondapplication.models.PollutionMeasurement;

public class DashboardActivity extends AppCompatActivity {
    private List<String> locationsList = new ArrayList<>();
    private Spinner locationsSpinner;
    private ArrayAdapter<String> adapter;
    private Integer pollutionValue;

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

        final Button addPollutionButton = findViewById(R.id.addPollutionButton);
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

        try {
            FileInputStream fileInputStream = openFileInput("locations.bin");
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            locationsList = (List<String>)objectInputStream.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        locationsSpinner = findViewById(R.id.locationSpinner);
        adapter = new ArrayAdapter<>(getApplicationContext(),
               R.layout.support_simple_spinner_dropdown_item, locationsList);
        locationsSpinner.setAdapter(adapter);

        SharedPreferences sharedPreferences = getSharedPreferences("settings", MODE_PRIVATE);
        int selectedItem = sharedPreferences.getInt("selectedItem", 0);
        locationsSpinner.setSelection(selectedItem);
        int value = sharedPreferences.getInt("pollutionValue", -1);
        if(value != -1) {
            TextView pollutionTextView = findViewById(R.id.pollutionTextView);
            pollutionTextView.setText(new Integer(value).toString());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            FileOutputStream fileOutputStream = openFileOutput("locations.bin", MODE_PRIVATE);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            if(locationsList != null) {
                objectOutputStream.writeObject(locationsList);
            }
            objectOutputStream.close();
        }
        catch(IOException e) {
            e.printStackTrace();
        }

        SharedPreferences sharedPreferences = getSharedPreferences("settings", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if(locationsSpinner != null && locationsSpinner.getSelectedItem() != null) {
            editor.putInt("selectedItem", locationsSpinner.getSelectedItemPosition());
        }
        if(pollutionValue != null) {
            editor.putInt("pollutionValue", pollutionValue);
        }
        editor.commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 100 && resultCode == RESULT_OK) {
            PollutionMeasurement measurement =
                    (PollutionMeasurement) data.getSerializableExtra("measurement");
            TextView pollutionTextView = findViewById(R.id.pollutionTextView);
            pollutionValue = measurement.getValue();
            pollutionTextView.setText(pollutionValue.toString());
            if(!locationsList.contains(measurement.getArea())) {
                locationsList.add(measurement.getArea());
            }
            adapter.notifyDataSetChanged();
        }
    }

    public void navigateToForecastList(View view) {
        Intent intent = new Intent(DashboardActivity.this, ListActivity.class);
        startActivity(intent);
    }
}
