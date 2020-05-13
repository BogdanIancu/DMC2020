package ro.mta.secondapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
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

        locationsSpinner = findViewById(R.id.locationSpinner);
        if(locationsSpinner != null) {
            locationsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String location = locationsSpinner.getSelectedItem().toString();
                    WeatherWorker worker = new WeatherWorker(location);
                    worker.start();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
        }

//        try {
//            FileInputStream fileInputStream = openFileInput("locations.bin");
//            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
//            locationsList = (List<String>)objectInputStream.readObject();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }

        adapter = new ArrayAdapter<>(getApplicationContext(),
                        R.layout.support_simple_spinner_dropdown_item, locationsList);
        locationsSpinner.setAdapter(adapter);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        String androidId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        DatabaseReference myRef = database.getReference(androidId).child("cities");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String> list = (List<String>)dataSnapshot.getValue();
                if(list != null) {
                    locationsList.clear();
                    locationsList.addAll(list);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

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
//        try {
//            FileOutputStream fileOutputStream = openFileOutput("locations.bin", MODE_PRIVATE);
//            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
//            if(locationsList != null) {
//                objectOutputStream.writeObject(locationsList);
//            }
//            objectOutputStream.close();
//        }
//        catch(IOException e) {
//            e.printStackTrace();
//        }
//
//        SharedPreferences sharedPreferences = getSharedPreferences("settings", MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        if(locationsSpinner != null && locationsSpinner.getSelectedItem() != null) {
//            editor.putInt("selectedItem", locationsSpinner.getSelectedItemPosition());
//        }
//        if(pollutionValue != null) {
//            editor.putInt("pollutionValue", pollutionValue);
//        }
//        editor.commit();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        String androidId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        DatabaseReference myRef = database.getReference(androidId).child("cities");
        myRef.setValue(locationsList);
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
        if(locationsSpinner != null && locationsSpinner.getSelectedItem() != null) {
            intent.putExtra("location", locationsSpinner.getSelectedItem().toString());
        }
        startActivity(intent);
    }

    public void navigateToChart(View view) {
        Intent intent = new Intent(DashboardActivity.this, ChartActivity.class);
        startActivity(intent);
    }

    class WeatherWorker extends Thread {
        private String location;

        public WeatherWorker(String location) {
            this.location = location;
        }

        @Override
        public void run() {
            String api = String.format("http://api.openweathermap.org/data/2.5/weather?q=%s&appid=7b10426ee90376dc3d6525f847128b35&units=metric&format=json&lang=en", location);
            try {
                URL url = new URL(api);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = connection.getInputStream();
                InputStreamReader reader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(reader);

                StringBuilder stringBuilder = new StringBuilder();
                String line;

                while((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line);
                }

                String result = stringBuilder.toString();
                JSONObject json = new JSONObject(result);
                JSONObject mainObject = json.getJSONObject("main");
                double value = mainObject.getDouble("temp");
                final int temperature = (int)value;
                final double feelsLike = mainObject.getDouble("feels_like");
                final int feltTemperature = (int)feelsLike;
                JSONArray weatherArray = json.getJSONArray("weather");
                JSONObject weatherObject = weatherArray.getJSONObject(0);
                final String description = weatherObject.getString("description");
                String iconValue = weatherObject.getString("icon");
                URL imageURL =
                        new URL(String.format("http://openweathermap.org/img/wn/%s@2x.png", iconValue));
                HttpURLConnection imageConnection = (HttpURLConnection) imageURL.openConnection();
                final Bitmap image = BitmapFactory.decodeStream(imageConnection.getInputStream());

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        TextView tempTextView = findViewById(R.id.temperatureTextView);
                        tempTextView.setText(temperature + "° C");

                        TextView condTextView = findViewById(R.id.conditionsTextView);
                        condTextView.setText(description);

                        TextView feelTextView = findViewById(R.id.feelTextView);
                        feelTextView.setText(String.format("Feels like: %d° C", feltTemperature));

                        ImageView imageView = findViewById(R.id.imageView);
                        imageView.setImageBitmap(image);
                    }
                });
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
