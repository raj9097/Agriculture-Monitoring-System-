package com.farmmonitor.agriai;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FarmDataFragment extends Fragment {

    private static final String ARG_FARM_ID = "farm_id";
    private static final long ONLINE_THRESHOLD = 60000; // 1 minute

    private String farmId;
    private DatabaseReference farmRef;
    private ValueEventListener farmListener;

    // Views
    private ImageView ivConnectionStatus;
    private TextView tvConnectionStatus, tvConnectionBadge;
    private TextView tvTemperature, tvHumidity, tvSoilMoisture, tvLightLevel;
    private TextView tvLastUpdate;
    private LinearLayout layoutNoData;

    public static FarmDataFragment newInstance(String farmId) {
        FarmDataFragment fragment = new FarmDataFragment();
        Bundle args = new Bundle();
        args.putString(ARG_FARM_ID, farmId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            farmId = getArguments().getString(ARG_FARM_ID);
        }

        // Initialize Firebase Database reference
        farmRef = FirebaseDatabase.getInstance().getReference("farms").child(farmId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_farm_data, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
        setupDataListener();
    }

    private void initViews(View view) {
        ivConnectionStatus = view.findViewById(R.id.iv_connection_status);
        tvConnectionStatus = view.findViewById(R.id.tv_connection_status);
        tvConnectionBadge = view.findViewById(R.id.tv_connection_badge);

        tvTemperature = view.findViewById(R.id.tv_temperature);
        tvHumidity = view.findViewById(R.id.tv_humidity);
        tvSoilMoisture = view.findViewById(R.id.tv_soil_moisture);
        tvLightLevel = view.findViewById(R.id.tv_light_level);

        tvLastUpdate = view.findViewById(R.id.tv_last_update);
        layoutNoData = view.findViewById(R.id.layout_no_data);
    }

    private void setupDataListener() {
        farmListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    SensorData data = snapshot.getValue(SensorData.class);
                    if (data != null) {
                        updateUI(data);
                        layoutNoData.setVisibility(View.GONE);
                    } else {
                        showNoDataState();
                    }
                } else {
                    showNoDataState();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                showNoDataState();
            }
        };
        farmRef.addValueEventListener(farmListener);
    }

    private void updateUI(SensorData data) {
        boolean isOnline = isDeviceOnline(data.getLastUpdate());
        updateConnectionStatus(isOnline);

        tvTemperature.setText(data.getTemperature() != null ?
                String.format("%.1f°C", data.getTemperature()) : "--°C");

        tvHumidity.setText(data.getHumidity() != null ?
                String.format("%.0f%%", data.getHumidity()) : "--%");

        tvSoilMoisture.setText(data.getSoilMoisture() != null ?
                String.format("%.0f%%", data.getSoilMoisture()) : "--%");

        tvLightLevel.setText(data.getLightLevel() != null ?
                String.format("%.0f lux", data.getLightLevel()) : "-- lux");

        if (data.getLastUpdate() != null) {
            String timeString = android.text.format.DateFormat.format(
                    "dd/MM/yyyy HH:mm:ss", data.getLastUpdate()).toString();
            tvLastUpdate.setText("Last updated: " + timeString);
            tvLastUpdate.setVisibility(View.VISIBLE);
        } else {
            tvLastUpdate.setVisibility(View.GONE);
        }
    }

    private void updateConnectionStatus(boolean isOnline) {
        if (isOnline) {
            ivConnectionStatus.setImageResource(R.drawable.ic_wifi);
            ivConnectionStatus.setColorFilter(ContextCompat.getColor(requireContext(), android.R.color.holo_green_dark));
            tvConnectionStatus.setText("Online");
            tvConnectionBadge.setText("ONLINE");
            tvConnectionBadge.setBackgroundResource(R.drawable.bg_status_online);
        } else {
            ivConnectionStatus.setImageResource(R.drawable.ic_wifi_off);
            ivConnectionStatus.setColorFilter(ContextCompat.getColor(requireContext(), android.R.color.holo_red_dark));
            tvConnectionStatus.setText("Offline");
            tvConnectionBadge.setText("OFFLINE");
            tvConnectionBadge.setBackgroundResource(R.drawable.bg_status_offline);
        }
    }

    private boolean isDeviceOnline(Long lastUpdate) {
        if (lastUpdate == null) return false;
        return (System.currentTimeMillis() - lastUpdate) < ONLINE_THRESHOLD;
    }

    private void showNoDataState() {
        layoutNoData.setVisibility(View.VISIBLE);
        tvConnectionStatus.setText("Offline");
        tvConnectionBadge.setText("OFFLINE");
        tvConnectionBadge.setBackgroundResource(R.drawable.bg_status_offline);
        ivConnectionStatus.setImageResource(R.drawable.ic_wifi_off);
        ivConnectionStatus.setColorFilter(ContextCompat.getColor(requireContext(), android.R.color.holo_red_dark));

        tvTemperature.setText("--°C");
        tvHumidity.setText("--%");
        tvSoilMoisture.setText("--%");
        tvLightLevel.setText("-- lux");
        tvLastUpdate.setVisibility(View.GONE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (farmRef != null && farmListener != null) {
            farmRef.removeEventListener(farmListener);
        }
    }
}
