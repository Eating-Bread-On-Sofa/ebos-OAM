package cn.edu.bjtu.ebosoam.service;

import cn.edu.bjtu.ebosoam.controller.OamController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import sun.misc.BASE64Decoder;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

@Component
@Order(3)
public class InitRecognized implements ApplicationRunner {

    @Autowired
    MqFactory mqFactory;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        new Thread(()->{
            MqConsumer mqConsumer = mqFactory.createConsumer("photo");
            int temp = 2;
            while (true){
                String base64 = mqConsumer.subscribe();
                if (base64 == null){
                    OamController.faceRecognized.put("url",null);
                    OamController.faceRecognized.put("message","陌生人图片加载失败！");
                    continue;
                }
                BASE64Decoder decoder = new BASE64Decoder();
                OutputStream out;
                try {
                    String imgPath = "/opt/photo/" + temp + ".jpg";
                    out = new FileOutputStream(imgPath);
                    byte[] b = decoder.decodeBuffer(base64);
                    for (int i = 0; i < b.length; ++i) {
                        if (b[i] < 0) {
                            b[i] += 256;
                        }
                    }
                    out.write(b);
                    out.flush();
                    out.close();
                    String url = "/image/" + temp + ".jpg";
                    OamController.faceRecognized.put("url",url);
                    OamController.faceRecognized.put("message","陌生人进入摄像头识别视野！");
                    if(temp>1000){
                        temp=2;
                    }else {
                        temp++;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
