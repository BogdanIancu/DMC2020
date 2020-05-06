package ro.mta.secondapplication.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.List;

import ro.mta.secondapplication.R;
import ro.mta.secondapplication.models.WeatherForecast;

public class CustomForecastAdapter extends ArrayAdapter<WeatherForecast> {
    private int itemResource;

    public CustomForecastAdapter(@NonNull Context context, int resource, @NonNull List<WeatherForecast> objects) {
        super(context, resource, objects);
        itemResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(itemResource, null);
        }

        WeatherForecast forecast = getItem(position);

        TextView temperatureTextView = convertView.findViewById(R.id.itemTemperatureTextView);
        temperatureTextView.setText(forecast.getTemperature() + "° C");

        TextView feelTextView = convertView.findViewById(R.id.itemFeelTextView);
        feelTextView.setText(String.format("Feels like %d° C", forecast.getFeltTemperature()));

        TextView conditionsTextView = convertView.findViewById(R.id.itemConditionsTextView);
        conditionsTextView.setText(forecast.getConditions());

        TextView dateTextView = convertView.findViewById(R.id.itemDateTextView);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");
        if(forecast.getDate() != null) {
            dateTextView.setText(simpleDateFormat.format(forecast.getDate()));
        }

        ImageView imageView = convertView.findViewById(R.id.itemImageView);
        imageView.setImageBitmap(forecast.getImage());

        return convertView;
    }
}
