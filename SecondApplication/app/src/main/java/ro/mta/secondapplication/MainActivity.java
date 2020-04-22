package ro.mta.secondapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import ro.mta.secondapplication.models.PollutionMeasurement;

public class MainActivity extends AppCompatActivity {
    private EditText areaEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        areaEditText = findViewById(R.id.areaEditText);

        Intent intent = getIntent();
        String value = intent.getStringExtra("location");
        areaEditText.setText(value);
    }

    public void save(View view) {
        PollutionMeasurement pollutionMeasurement =
                new PollutionMeasurement();
        EditText dateEditText = findViewById(R.id.datEditText);
        EditText valueEditText = findViewById(R.id.valueEditText);

        String area = areaEditText.getText().toString();
        pollutionMeasurement.setArea(area);

        SimpleDateFormat simpleDateFormat =
                new SimpleDateFormat("dd.MM.yyyy");

        String dateText = dateEditText.getText().toString();
        try {
            Date date = simpleDateFormat.parse(dateText);
            pollutionMeasurement.setDate(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String valueText = valueEditText.getText().toString();
        int value = Integer.parseInt(valueText);
        pollutionMeasurement.setValue(value);
        Intent intent = new Intent();
        intent.putExtra("measurement", pollutionMeasurement);
        setResult(RESULT_OK, intent);
        finish();
    }
}
