package cn.edu.bjtu.ebosoam.entity;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "recognized_name")
public class Recognized {

    String name;
    String time;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
