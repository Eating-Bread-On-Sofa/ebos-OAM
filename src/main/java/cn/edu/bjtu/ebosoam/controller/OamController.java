package cn.edu.bjtu.ebosoam.controller;

import cn.edu.bjtu.ebosoam.entity.Recognized;
import cn.edu.bjtu.ebosoam.service.*;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@RequestMapping("/api")
@RestController
public class OamController {
    @Autowired
    LogService logService;
    @Autowired
    RestTemplate restTemplate;
    @Autowired
    SubscribeService subscribeService;
    @Autowired
    MqFactory mqFactory;
    @Autowired
    RecognizedService recognizedService;

    public static final List<RawSubscribe> status = new LinkedList<>();
    private ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(1, 50,3, TimeUnit.SECONDS,new SynchronousQueue<>());
    public static JSONObject faceRecognized = new JSONObject();

    @ApiOperation(value = "微服务订阅mq的主题")
    @CrossOrigin
    @PostMapping("/subscribe")
    public String newSubscribe(RawSubscribe rawSubscribe){
        if(!OamController.check(rawSubscribe.getSubTopic())){
            try{
                status.add(rawSubscribe);
                subscribeService.save(rawSubscribe.getSubTopic());
                threadPoolExecutor.execute(rawSubscribe);
                logService.info("create","运维监控成功订阅主题"+ rawSubscribe.getSubTopic());
                return "订阅成功";
            }catch (Exception e) {
                e.printStackTrace();
                logService.error("create","运维监控订阅主题"+rawSubscribe.getSubTopic()+"时，参数设定有误。");
                return "参数错误!";
            }
        }else {
            logService.error("create","运维监控已订阅主题"+rawSubscribe.getSubTopic()+",再次订阅失败");
            return "订阅主题重复";
        }
    }

    public static boolean check(String subTopic){
        boolean flag = false;
        for (RawSubscribe rawSubscribe : status) {
            if(subTopic.equals(rawSubscribe.getSubTopic())){
                flag=true;
                break;
            }
        }
        return flag;
    }

    @ApiOperation(value = "删除微服务订阅mq的主题")
    @CrossOrigin
    @DeleteMapping("/subscribe/{subTopic}")
    public boolean delete(@PathVariable String subTopic){
        boolean flag;
        synchronized (status){
            flag = status.remove(search(subTopic));
        }
        return flag;
    }

    public static RawSubscribe search(String subTopic){
        for (RawSubscribe rawSubscribe : status) {
            if(subTopic.equals(rawSubscribe.getSubTopic())){
                return rawSubscribe;
            }
        }
        return null;
    }

    @ApiOperation(value = "微服务向mq的某主题发布消息")
    @CrossOrigin
    @PostMapping("/publish")
    public String publish(@RequestParam(value = "topic") String topic,@RequestParam(value = "message") String message){
        MqProducer mqProducer = mqFactory.createProducer();
        mqProducer.publish(topic,message);
        return "发布成功";
    }

    @ApiOperation(value = "显示最近100条人脸识别信息", notes = "前端每次刚打开人脸识别界面调用")
    @CrossOrigin
    @GetMapping("/recent")
    public List<Recognized> getRecent(){
        List<Recognized> result = recognizedService.findRecent();
        Collections.reverse(result);
        return result;
    }

    @ApiOperation(value = "通过人名搜索识别结果", notes = "前端人脸识别界面搜索时调用")
    @CrossOrigin
    @GetMapping("/log")
    public List<Recognized> getRecognized(String name){
        List<Recognized> result = recognizedService.find(name);
        Collections.reverse(result);
        return result;
    }

    @ApiOperation(value = "陌生人出现告警", notes = "陌生人出现告警")
    @CrossOrigin
    @GetMapping("/face")
    public JSONObject getRecognizedWarn(){
        JSONObject result = new JSONObject();
        if(faceRecognized.equals(new JSONObject())){
            return faceRecognized;
        }
        result = faceRecognized;
        faceRecognized = new JSONObject();
        return result;
    }

    @ApiOperation(value = "微服务运行检测", notes = "微服务正常运行时返回 pong")
    @CrossOrigin
    @GetMapping("/ping")
    public String ping(){
        return "pong";
    }
}
