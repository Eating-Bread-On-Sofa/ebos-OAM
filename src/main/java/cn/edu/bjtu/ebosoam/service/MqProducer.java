package cn.edu.bjtu.ebosoam.service;

public interface MqProducer {
    void publish(String topic, String message);
}
