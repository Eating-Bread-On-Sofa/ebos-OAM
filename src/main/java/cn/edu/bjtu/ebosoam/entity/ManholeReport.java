package cn.edu.bjtu.ebosoam.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

/**
 * Create by ZhiYuan
 * data:2021/5/10
 */
@Document(collection = "manhole_report")
public class ManholeReport implements Serializable {

    @Id
    String deviceID;
    String deviceName;
    String battery_value;
    int rssi;
    float longitude;
    float latitude;

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }

    public String getBattery_value() {
        return battery_value;
    }

    public void setBattery_value(String battery_value) {
        this.battery_value = battery_value;
    }

    public int getRssi() {
        return rssi;
    }

    public void setRssi(int rssi) {
        this.rssi = rssi;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }
}
