package ro.mta.secondapplication.workers;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ListView;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import ro.mta.secondapplication.R;
import ro.mta.secondapplication.adapters.CustomForecastAdapter;
import ro.mta.secondapplication.models.WeatherForecast;

public class ForecastWorker extends AsyncTask<String, Void, List<WeatherForecast>> {
    private ListView listView;
    private Context context;

    public ForecastWorker(ListView listView, Context context) {
        this.listView = listView;
        this.context = context;
    }

    @Override
    protected List<WeatherForecast> doInBackground(String... strings) {
        List<WeatherForecast> list = new ArrayList<>();
        if(strings != null && strings.length > 0) {
            String location = strings[0];
            String forecastApi = String.format("http://api.openweathermap.org/data/2.5/forecast/daily?q=%s&appid=7b10426ee90376dc3d6525f847128b35&mode=xml&units=metric&cnt=7&lang=en", location);
            try {
                URL url = new URL(forecastApi);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                InputStream inputStream = connection.getInputStream();
                DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                Document xml = builder.parse(inputStream);

                NodeList nodes = xml.getElementsByTagName("time");
                for(int i = 0; i < nodes.getLength(); i++) {
                    WeatherForecast weather = new WeatherForecast();
                    Element node = (Element)nodes.item(i);
                    String day = node.getAttribute("day");
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    weather.setDate(simpleDateFormat.parse(day));
                    Element temp = (Element)node.getElementsByTagName("temperature").item(0);
                    String tempValue = temp.getAttribute("day");
                    double temperature = Double.parseDouble(tempValue);
                    weather.setTemperature((int)temperature);

                    Element symbol = (Element)node.getElementsByTagName("symbol").item(0);
                    String desc = symbol.getAttribute("name");
                    weather.setConditions(desc);

                    String icon = symbol.getAttribute("var");
                    URL imageURl = new URL(String.format("http://openweathermap.org/img/wn/%s@2x.png", icon));
                    HttpURLConnection imageConnection = (HttpURLConnection) imageURl.openConnection();
                    Bitmap image = BitmapFactory.decodeStream(imageConnection.getInputStream());
                    weather.setImage(image);
                    list.add(weather);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    @Override
    protected void onPostExecute(List<WeatherForecast> weatherForecasts) {
        CustomForecastAdapter adapter =
                new CustomForecastAdapter(context,
                        R.layout.forecast_item_layout, weatherForecasts);
        listView.setAdapter(adapter);
    }
}
