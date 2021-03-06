package cn.edu.bjtu.ebosoam.service;

import cn.edu.bjtu.ebosoam.controller.OamController;
import cn.edu.bjtu.ebosoam.entity.Subscribe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Component
@Order(2)
public class InitRawSubscribe implements ApplicationRunner {

    @Autowired
    SubscribeService subscribeService;
    @Autowired
    LogService logService;

    private ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(1, 50,3, TimeUnit.SECONDS,new SynchronousQueue<>());

    @Override
    public void run(ApplicationArguments args) throws Exception {
        List<Subscribe> subscribes = subscribeService.findByServiceName();

        for (Subscribe subscribe : subscribes){
            RawSubscribe rawSubscribe = new RawSubscribe(subscribe.getSubTopic());
            OamController.status.add(rawSubscribe);
            logService.info("update","运维监控重启后，初始化启动订阅："+rawSubscribe);
            threadPoolExecutor.execute(rawSubscribe);
        }
    }
}
