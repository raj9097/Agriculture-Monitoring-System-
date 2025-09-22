package com.farmmonitor.agriai;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;
import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity {

    private Spinner spinnerFarms;
    private LinearLayout mainLayout;
    private FirebaseAuth mAuth;

    private String[] farms = {"Farm 1", "Farm 2", "Farm 3", "Farm 4"};
    private String selectedFarmId = "farm_1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();

        createLayout();
        setupFarmSpinner();

        // Load initial fragment
        loadFarmDataFragment(selectedFarmId);
    }

    private void createLayout() {
        // Create main layout
        mainLayout = new LinearLayout(this);
        mainLayout.setOrientation(LinearLayout.VERTICAL);

        // Create toolbar
        Toolbar toolbar = new Toolbar(this);
        toolbar.setTitle("Farm Monitor");
        toolbar.setBackgroundColor(getResources().getColor(android.R.color.holo_green_dark));
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        setSupportActionBar(toolbar);

        // Farm selection section
        LinearLayout farmSection = new LinearLayout(this);
        farmSection.setOrientation(LinearLayout.HORIZONTAL);
        farmSection.setPadding(32, 24, 32, 24);
        farmSection.setBackgroundColor(getResources().getColor(android.R.color.white));

        TextView farmLabel = new TextView(this);
        farmLabel.setText("Select Farm: ");
        farmLabel.setTextSize(16);
        farmLabel.setTextColor(getResources().getColor(android.R.color.black));
        LinearLayout.LayoutParams labelParams = new LinearLayout.LayoutParams(0,
                LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
        farmLabel.setLayoutParams(labelParams);

        // Create spinner
        spinnerFarms = new Spinner(this);
        LinearLayout.LayoutParams spinnerParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        spinnerParams.setMargins(16, 0, 0, 0);
        spinnerFarms.setLayoutParams(spinnerParams);

        farmSection.addView(farmLabel);
        farmSection.addView(spinnerFarms);

        // Add views to main layout
        mainLayout.addView(toolbar);
        mainLayout.addView(farmSection);

        setContentView(mainLayout);
    }

    private void setupFarmSpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, farms);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFarms.setAdapter(adapter);

        spinnerFarms.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String farmName = farms[position];
                selectedFarmId = farmName.toLowerCase().replace(" ", "_");
                loadFarmDataFragment(selectedFarmId);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });
    }

    private void loadFarmDataFragment(String farmId) {
        // Remove existing fragment view if any
        View existingFragment = findViewById(999); // Fragment container ID
        if (existingFragment != null) {
            mainLayout.removeView(existingFragment);
        }

        // Create new fragment container
        LinearLayout fragmentContainer = new LinearLayout(this);
        fragmentContainer.setId(999);
        fragmentContainer.setOrientation(LinearLayout.VERTICAL);
        fragmentContainer.setPadding(24, 24, 24, 24);
        fragmentContainer.setBackgroundColor(getResources().getColor(android.R.color.white));

        LinearLayout.LayoutParams containerParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 0, 1.0f);
        containerParams.setMargins(0, 16, 0, 0);
        fragmentContainer.setLayoutParams(containerParams);

        // Add farm data views
        addSensorDataViews(fragmentContainer, farmId);

        mainLayout.addView(fragmentContainer);
    }

    private void addSensorDataViews(LinearLayout container, String farmId) {
        // Title
        TextView title = new TextView(this);
        title.setText("Sensor Data for " + farmId.replace("_", " ").toUpperCase());
        title.setTextSize(20);
        title.setTextColor(getResources().getColor(android.R.color.black));
        title.setPadding(0, 0, 0, 24);
        container.addView(title);

        // Connection Status
        LinearLayout statusLayout = new LinearLayout(this);
        statusLayout.setOrientation(LinearLayout.HORIZONTAL);
        statusLayout.setPadding(16, 16, 16, 16);
        statusLayout.setBackgroundColor(getResources().getColor(android.R.color.background_light));

        TextView statusIcon = new TextView(this);
        statusIcon.setText("📶 ");
        statusIcon.setTextSize(24);

        TextView statusText = new TextView(this);
        statusText.setText("Raspberry Pi Status: ONLINE");
        statusText.setTextSize(16);
        statusText.setTextColor(getResources().getColor(android.R.color.holo_green_dark));

        statusLayout.addView(statusIcon);
        statusLayout.addView(statusText);
        container.addView(statusLayout);

        // Add spacing
        View spacer1 = new View(this);
        spacer1.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 24));
        container.addView(spacer1);

        // Sensor readings
        addSensorCard(container, "🌡️ Temperature", "25.3°C", android.R.color.holo_orange_light);
        addSensorCard(container, "💧 Humidity", "62%", android.R.color.holo_blue_light);
        addSensorCard(container, "🌱 Soil Moisture", "48%", android.R.color.holo_green_light);
        addSensorCard(container, "☀️ Light Level", "850 lux", android.R.color.holo_orange_light);

        // Last update info
        TextView lastUpdate = new TextView(this);
        lastUpdate.setText("Last updated: " + new java.util.Date().toString());
        lastUpdate.setTextSize(12);
        lastUpdate.setTextColor(getResources().getColor(android.R.color.darker_gray));
        lastUpdate.setPadding(0, 24, 0, 0);
        container.addView(lastUpdate);
    }

    private void addSensorCard(LinearLayout parent, String label, String value, int colorRes) {
        LinearLayout cardLayout = new LinearLayout(this);
        cardLayout.setOrientation(LinearLayout.HORIZONTAL);
        cardLayout.setPadding(20, 16, 20, 16);
        cardLayout.setBackgroundColor(getResources().getColor(colorRes));

        LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        cardParams.setMargins(0, 0, 0, 12);
        cardLayout.setLayoutParams(cardParams);

        TextView labelView = new TextView(this);
        labelView.setText(label);
        labelView.setTextSize(16);
        labelView.setTextColor(getResources().getColor(android.R.color.white));
        LinearLayout.LayoutParams labelParams = new LinearLayout.LayoutParams(0,
                LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
        labelView.setLayoutParams(labelParams);

        TextView valueView = new TextView(this);
        valueView.setText(value);
        valueView.setTextSize(18);
        valueView.setTextColor(getResources().getColor(android.R.color.white));
        valueView.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);

        cardLayout.addView(labelView);
        cardLayout.addView(valueView);
        parent.addView(cardLayout);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 1, 0, "Logout").setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == 1) {
            mAuth.signOut();
            Intent intent = new Intent(this, StartingActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}