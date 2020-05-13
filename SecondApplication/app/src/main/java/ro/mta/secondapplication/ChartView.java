package ro.mta.secondapplication;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import ro.mta.secondapplication.models.WeatherForecast;

public class ChartView extends View {
    public List<WeatherForecast> temperatures = new ArrayList<>();

    public ChartView(Context context) {
        super(context);
    }

    public ChartView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = canvas.getWidth() / (temperatures.size() - 1);
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        for(WeatherForecast f : temperatures) {
            if(f.getTemperature() < min) {
                min = f.getTemperature();
            }
            if(f.getTemperature() > max) {
                max = f.getTemperature();
            }
        }
        int height = canvas.getHeight() / (max - min);

        for(int i = 0; i < temperatures.size() - 1; i++) {
            int value1 = temperatures.get(i).getTemperature();
            int value2 = temperatures.get(i+1).getTemperature();
            int startX = i * width;
            int stopX = (i+1) * width;
            int startY = value1 * height;
            int stopY = value2 * height;

            Paint paint = new Paint();
            paint.setColor(Color.BLUE);
            paint.setStrokeWidth(5);
            canvas.drawLine(startX, startY, stopX, stopY, paint);
        }
    }
}
