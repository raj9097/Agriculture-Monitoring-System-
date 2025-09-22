package com.farmmonitor.agriai;


import androidx.annotation.NonNull;

public class SensorData {
    private Double temperature;
    private Double humidity;
    private Double soilMoisture;
    private Double lightLevel;
    private Long lastUpdate;

    public SensorData() {
        // Default constructor required for calls to DataSnapshot.getValue(SensorData.class)
    }

    public SensorData(Double temperature, Double humidity, Double soilMoisture, Double lightLevel, Long lastUpdate) {
        this.temperature = temperature;
        this.humidity = humidity;
        this.soilMoisture = soilMoisture;
        this.lightLevel = lightLevel;
        this.lastUpdate = lastUpdate;
    }

    // Getters
    public Double getTemperature() {
        return temperature;
    }

    public Double getHumidity() {
        return humidity;
    }

    public Double getSoilMoisture() {
        return soilMoisture;
    }

    public Double getLightLevel() {
        return lightLevel;
    }

    public Long getLastUpdate() {
        return lastUpdate;
    }

    // Setters
    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    public void setHumidity(Double humidity) {
        this.humidity = humidity;
    }

    public void setSoilMoisture(Double soilMoisture) {
        this.soilMoisture = soilMoisture;
    }

    public void setLightLevel(Double lightLevel) {
        this.lightLevel = lightLevel;
    }

    public void setLastUpdate(Long lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    @NonNull
    @Override
    public String toString() {
        return "SensorData{" +
                "temperature=" + temperature +
                ", humidity=" + humidity +
                ", soilMoisture=" + soilMoisture +
                ", lightLevel=" + lightLevel +
                ", lastUpdate=" + lastUpdate +
                '}';
    }
}
