// Define package for this Activity
package com.example.myfinaltopapplication;
// Import Android base classes
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
// Import UI components
import android.widget.ImageButton;
import android.widget.TextView;
// Import AppCompatActivity as base class
import androidx.appcompat.app.AppCompatActivity;
// Import JSON handling
import org.json.JSONObject;
// Import Java utilities
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;
// Import MPAndroidChart classes
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;

// -------------------------------------------------------------
// WaterChartActivity
// Purpose: Display user’s water history (last N days) in a bar chart
// -------------------------------------------------------------
public class WaterChartActivity extends AppCompatActivity {

    // BarChart view object
    private BarChart barChart;

    // Title for chart
    private TextView chartTitle;

    // Back button
    private ImageButton backButton;

    // -----------------------------------------------------------------
    // onCreate - called when this Activity is created
    // -----------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Call parent constructor
        super.onCreate(savedInstanceState);

        // Load the layout XML (activity_water_chart.xml)
        setContentView(R.layout.activity_water_chart);

        // Link UI components
        barChart = findViewById(R.id.barChart);
        chartTitle = findViewById(R.id.chartTitle);
        backButton = findViewById(R.id.imageButton);

        // Read username from SharedPreferences (saved at login)
        SharedPreferences prefs = getSharedPreferences(getString(R.string.myprefs), MODE_PRIVATE);
        String username = prefs.getString(getString(R.string.currentuser), "guest");

        // Number of days to show in chart
        int days = 7;

        // Call REST client to get water history
        CompletableFuture<JSONObject> future = RestClient.getWaterHistoryMap(username, days);

        // Handle the async response
        future.thenAccept(obj -> runOnUiThread(() -> {
            try {
                // If response is null → no data
                if (obj == null) {
                    chartTitle.setText("No data available");
                    return;
                }

                Log.d("CHART_DATA", "Got JSON: " + obj.toString());

                // Create list of BarEntries
                ArrayList<BarEntry> entries = new ArrayList<>();

                // Create list of labels (dates)
                ArrayList<String> labels = new ArrayList<>();

                // Iterate over JSON keys (dates)
                Iterator<String> keys = obj.keys();
                while (keys.hasNext()) {
                    String date = keys.next();
                    int amount = obj.optInt(date, 0);

                    // Add entry (index temporarily = labels.size())
                    entries.add(new BarEntry(labels.size(), amount));

                    // Shorten date format → keep only MM-dd
                    if (date.length() >= 10) {
                        labels.add(date.substring(5)); // "2025-09-29" → "09-29"
                    } else {
                        labels.add(date);
                    }
                }

                // ✅ Sort entries and labels together by date order
                List<Integer> indices = new ArrayList<>();
                for (int i = 0; i < labels.size(); i++) indices.add(i);

                // Sort by original string date (obj keys) for correct order
                Collections.sort(indices, Comparator.comparingInt(i -> labels.get(i).hashCode()));

                ArrayList<BarEntry> sortedEntries = new ArrayList<>();
                ArrayList<String> sortedLabels = new ArrayList<>();
                int index = 0;
                for (int i : indices) {
                    BarEntry e = entries.get(i);
                    sortedEntries.add(new BarEntry(index, e.getY()));
                    sortedLabels.add(labels.get(i));
                    index++;
                }

                // Create dataset for BarChart
                BarDataSet dataSet = new BarDataSet(sortedEntries, "Water (ml)");

                // Set color for bars
                dataSet.setColor(getResources().getColor(android.R.color.holo_blue_light));

                // Create BarData object with dataset
                BarData data = new BarData(dataSet);

                // Set bar width
                data.setBarWidth(0.9f);

                // Assign data to chart
                barChart.setData(data);

                // ✅ Configure X-axis properly
                XAxis xAxis = barChart.getXAxis();
                xAxis.setGranularity(1f); // step = 1
                xAxis.setGranularityEnabled(true);
                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                xAxis.setCenterAxisLabels(false);
                xAxis.setLabelRotationAngle(-45f); // rotate dates
                xAxis.setTextSize(10f); // smaller text size

                // ✅ Format labels on X-axis
                xAxis.setValueFormatter(new ValueFormatter() {
                    @Override
                    public String getFormattedValue(float value) {
                        int i = (int) value;
                        if (i >= 0 && i < sortedLabels.size()) {
                            return sortedLabels.get(i);
                        }
                        return "";
                    }
                });

                // ✅ Add padding so bars won’t cut
                barChart.setFitBars(true);
                barChart.setExtraOffsets(16f, 16f, 16f, 32f);

                // ✅ Hide the "Water (ml)" legend at bottom-left
                barChart.getLegend().setEnabled(false);

                // Refresh chart
                barChart.invalidate();

                // Set chart title
                chartTitle.setText("Water History - Last " + days + " days");

            } catch (Exception e) {
                // Log error
                Log.e("CHART", "Error displaying chart", e);
                chartTitle.setText("Error loading chart");
            }
        }));

        backButton.setOnClickListener(view -> {
            Intent intent = new Intent(WaterChartActivity.this, HomePage.class);
            startActivity(intent);
        });
    }
}
