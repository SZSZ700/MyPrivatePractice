// Define package for the application
package com.example.myfinaltopapplication;

// Import Android base classes for UI and logging
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;

// Import AppCompatActivity as base class for Activity
import androidx.appcompat.app.AppCompatActivity;

// Import JSON handling for parsing server responses
import org.json.JSONObject;

// Import Java utilities for formatting, collections, and concurrency
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

// Import MPAndroidChart library for chart rendering
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;

// ------------------------------------------------------
// WaterChartActivity
// Purpose: Show user’s water consumption in 2 charts:
// 1. Last 7 days (daily amounts)
// 2. Weekly averages (4 weeks)
// ------------------------------------------------------
public class WaterChartActivity extends AppCompatActivity {

    // Bar chart for last 7 days
    private BarChart barChart7days;

    // Bar chart for weekly averages
    private BarChart barChartWeekly;

    // Title above charts
    private TextView chartTitle;

    // Back button for navigation
    private ImageButton backButton;

    // ----------------------------------------------------------------
    // onCreate – entry point when activity is created
    // ----------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Call parent constructor
        super.onCreate(savedInstanceState);

        // Inflate the layout XML file for this Activity
        setContentView(R.layout.activity_water_chart);

        // Link UI components to Java objects
        barChart7days = findViewById(R.id.barChart7days);   // Chart for 7 days
        barChartWeekly = findViewById(R.id.barChartWeekly); // Chart for weekly averages
        chartTitle = findViewById(R.id.chartTitle);         // Title text
        backButton = findViewById(R.id.imageButton);        // Back button

        // Retrieve logged-in username from SharedPreferences (fallback = "guest")
        String username = getSharedPreferences(getString(R.string.myprefs), MODE_PRIVATE)
                .getString(getString(R.string.currentuser), "guest");

        // Number of days for daily chart (last 7 days)
        int days = 7;

        // --------------------------------------------------
        // 1. Fetch last 7 days data from REST client
        // --------------------------------------------------
        CompletableFuture<JSONObject> future = RestClient.getWaterHistoryMap(username, days);

        // Handle asynchronous response
        future.thenAccept(obj -> runOnUiThread(() -> {
            try {
                // If no data returned → show message
                if (obj == null) {
                    chartTitle.setText("No data available");
                    return;
                }

                // Log received JSON for debugging
                Log.d("CHART_DATA", "Got JSON: " + obj.toString());

                // Prepare chart entries (bars) and labels (X-axis)
                ArrayList<BarEntry> entries = new ArrayList<>();
                ArrayList<String> labels = new ArrayList<>();

                // Collect JSON keys (dates)
                Iterator<String> keys = obj.keys();
                ArrayList<String> sortedKeys = new ArrayList<>();
                while (keys.hasNext()) sortedKeys.add(keys.next());

                // Sort the dates in ascending order
                Collections.sort(sortedKeys, Comparator.naturalOrder());

                // Convert each JSON value into a BarEntry
                int index = 0;
                for (String date : sortedKeys) {
                    // Get water amount for this date
                    int amount = obj.optInt(date, 0);

                    // Add entry (X=index, Y=amount)
                    entries.add(new BarEntry(index, amount));

                    // Format date to short (MM-dd) for labels
                    try {
                        String shortLabel = new SimpleDateFormat("MM-dd", Locale.getDefault())
                                .format(new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(date));
                        labels.add(shortLabel);
                    } catch (Exception e) {
                        // Fallback to original date if parsing fails
                        labels.add(date);
                    }
                    index++;
                }

                // Create dataset for 7-day chart
                BarDataSet dataSet = new BarDataSet(entries, "");
                dataSet.setColor(getResources().getColor(android.R.color.holo_blue_light));

                // Configure dataset appearance
                BarData data = new BarData(dataSet);
                data.setBarWidth(0.7f);       // Bar width
                data.setValueTextSize(10f);   // Value text size above bar

                // Assign dataset to chart
                barChart7days.setData(data);

                // Configure X-axis for 7-day chart
                XAxis xAxis = barChart7days.getXAxis();
                xAxis.setGranularity(1f);                     // Step size = 1
                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); // Labels at bottom
                xAxis.setDrawGridLines(false);                // Remove grid lines
                xAxis.setLabelRotationAngle(-45);             // Rotate labels
                xAxis.setTextSize(10f);                       // Label size

                // Custom formatter to show correct labels
                xAxis.setValueFormatter(new ValueFormatter() {
                    @Override
                    public String getFormattedValue(float value) {
                        int i = (int) value;
                        if (i >= 0 && i < labels.size()) return labels.get(i);
                        return "";
                    }
                });

                // Remove legend and description from chart
                barChart7days.getLegend().setEnabled(false);
                barChart7days.getDescription().setEnabled(false);

                // Refresh chart display
                barChart7days.invalidate();

                // Update chart title
                chartTitle.setText("Water History - Last " + days + " days");

            } catch (Exception e) {
                // Handle errors gracefully
                Log.e("CHART", "Error displaying 7-day chart", e);
                chartTitle.setText("Error loading chart");
            }
        }));

        // --------------------------------------------------
        // 2. Fetch weekly averages data from REST client
        // --------------------------------------------------
        CompletableFuture<Map<String, Integer>> futureWeekly = RestClient.getWeeklyAverages(username);

        // Handle asynchronous response
        futureWeekly.thenAccept(weeklyMap -> runOnUiThread(() -> {
            try {
                // If weekly map is empty or null
                if (weeklyMap == null || weeklyMap.isEmpty()) {
                    Log.w("CHART_WEEKLY", "No weekly averages found");
                    return;
                }

                // Prepare entries for weekly chart
                ArrayList<BarEntry> weeklyEntries = new ArrayList<>();
                ArrayList<String> weekLabels = new ArrayList<>();

                // Convert weekly averages into chart entries
                int idx = 0;
                for (Map.Entry<String, Integer> entry : weeklyMap.entrySet()) {
                    weeklyEntries.add(new BarEntry(idx, entry.getValue()));
                    weekLabels.add(entry.getKey());
                    idx++;
                }

                // Create dataset for weekly chart
                BarDataSet weeklySet = new BarDataSet(weeklyEntries, "");
                weeklySet.setColor(getResources().getColor(android.R.color.holo_green_light));

                // Configure dataset styling
                BarData weeklyData = new BarData(weeklySet);
                weeklyData.setBarWidth(0.5f);       // Bar width
                weeklyData.setValueTextSize(10f);   // Value text size

                // Assign dataset to weekly chart
                barChartWeekly.setData(weeklyData);

                // Configure X-axis for weekly chart
                XAxis xAxisW = barChartWeekly.getXAxis();
                xAxisW.setGranularity(1f);                     // Step size = 1
                xAxisW.setPosition(XAxis.XAxisPosition.BOTTOM); // Labels at bottom
                xAxisW.setDrawGridLines(false);                // Remove grid lines
                xAxisW.setTextSize(12f);                       // Label size

                // Custom formatter for week labels
                xAxisW.setValueFormatter(new ValueFormatter() {
                    @Override
                    public String getFormattedValue(float value) {
                        int i = (int) value;
                        if (i >= 0 && i < weekLabels.size()) return weekLabels.get(i);
                        return "";
                    }
                });

                // Remove legend and description
                barChartWeekly.getLegend().setEnabled(false);
                barChartWeekly.getDescription().setEnabled(false);

                // Refresh chart display
                barChartWeekly.invalidate();

            } catch (Exception e) {
                // Handle error in weekly chart
                Log.e("CHART_WEEKLY", "Error displaying weekly chart", e);
            }
        }));

        backButton.setOnClickListener(view -> {
            Intent intent = new Intent(this, HomePage.class);
            startActivity(intent);
        });
    }
}
