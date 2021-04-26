package cn.edu.bjtu.ebosoam.controller;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RequestMapping("/manhole")
@RestController
public class ManholeController {

    @Autowired
    RestTemplate restTemplate;

//    @Autowired
//    MongoTemplate mongoTemplate;

    @ApiOperation(value = "将数据传到device-rest-go")
    @CrossOrigin
    @PostMapping("/{id}/{deviceResources}")
    public void sub(@PathVariable String id, @PathVariable String deviceResources,@RequestBody JSONObject result){
        String url = "http://localhost:48081/api/v1/device/" + id;
        JSONObject js = new JSONObject();
        String deviceName = "name";
        try {
            js = restTemplate.getForObject(url, JSONObject.class);
            deviceName = js.getString("name");
        }catch (Exception ignored){}
       String api = "http://localhost:49986/api/v1/resource/" + deviceName + "/" + deviceResources;
        try {
            restTemplate.postForObject(api,result,JSONObject.class);
        }catch (Exception ignored){}
    }
}
