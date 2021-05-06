package cn.edu.bjtu.ebosoam.entity;

import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Date;

/**
 * Create by ZhiYuan
 * data:2021/5/6
 */

@Document(collection = "manhole_event")
public class ManholeEvent implements Serializable {
    String deviceName;
    String eventContent;
    Date time;

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getEventContent() {
        return eventContent;
    }

    public void setEventContent(String eventContent) {
        this.eventContent = eventContent;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}
