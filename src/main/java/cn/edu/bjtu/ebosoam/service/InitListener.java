package cn.edu.bjtu.ebosoam.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(1)
public class InitListener implements ApplicationRunner {
    @Autowired
    MqFactory mqFactory;
    @Value("${mq}")
    private String name;

    @Override
    public void run(ApplicationArguments arguments) {
        new Thread(() -> {
            MqConsumer mqConsumer = mqFactory.createConsumer("notice");
            while (true) {
                JSONObject msg = JSON.parseObject(mqConsumer.subscribe());
                System.out.println("收到：" + msg);
            }
        }).start();
    }
}
