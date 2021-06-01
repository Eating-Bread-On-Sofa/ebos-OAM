package cn.edu.bjtu.ebosoam.controller;

import cn.edu.bjtu.ebosoam.entity.ManholeEvent;
import cn.edu.bjtu.ebosoam.entity.ManholeReport;
import cn.edu.bjtu.ebosoam.service.ManholeEventService;
import cn.edu.bjtu.ebosoam.service.ManholeReportService;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RequestMapping("/manhole")
@RestController
public class ManholeController {

    @Autowired
    RestTemplate restTemplate;
    @Autowired
    ManholeEventService manholeEventService;
    @Autowired
    ManholeReportService manholeReportService;

    public static JSONObject eventWarning = new JSONObject();

    @ApiOperation(value = "将数据传到device-rest-go")
    @CrossOrigin
    @PostMapping("/{id}/{deviceResources}")
    public void sub(@PathVariable String id, @PathVariable String deviceResources,@RequestBody JSONObject result) throws ParseException {
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
        if (result.getString("messageType").equals("eventReport")){
            eventReport(result,deviceName);
        }
        if (result.getString("messageType").equals("dataReport")){
            dataReport(result,id,deviceName);
        }
    }

    public void dataReport(JSONObject result,String id,String deviceName){
        if (result.getIntValue("serviceId")==3){
            JSONObject payload = result.getJSONObject("payload");
            ManholeReport manhole = new ManholeReport();
            manhole.setDeviceID(id);
            manhole.setDeviceName(deviceName);
            manhole.setBattery_value(payload.getIntValue("battery_value") + "%");
            manhole.setRssi(payload.getIntValue("rssi"));
            manhole.setLongitude(payload.getFloatValue("longitude"));
            manhole.setLatitude(payload.getFloatValue("latitude"));
            ManholeReport manholeReport = manholeReportService.findByName(deviceName);
            if (manholeReport == null){
                manholeReportService.write(manhole);
            }else {
                manholeReportService.remove(id);
                manholeReportService.write(manhole);
            }
        }
    }

    public void eventReport(JSONObject result,String deviceName) throws ParseException {
        JSONObject js = new JSONObject();
        js = result.getJSONObject("eventContent");
        String content = "";
        Date time = stampToDate(result.getString("timestamp"));
        switch (result.getIntValue("serviceId")){
            case 1001:
                if (js.getIntValue("manhole_cover_position_state") == 1){
                    content = deviceName + "监测井盖位置偏移告警！";
                    eventWarning.put("url","/image/1.jpg");
                    eventWarning.put("message",content);
                }else {
                    content = deviceName + "监测井盖位置偏移后复原！";
                    eventWarning.put("url","/image/1.jpg");
                    eventWarning.put("message",content);
                }
                break;
            case 1002:
                DecimalFormat df = new DecimalFormat("#.00");
                content = deviceName + "监测井盖电池低压告警，电压值为：" + df.format(js.getDoubleValue("battery_voltage")) + "！";
                eventWarning.put("url","/image/1.jpg");
                eventWarning.put("message",content);
                break;
            case 1003:
                if (js.getIntValue("water_level_state") == 1){
                    content = deviceName + "监测井盖水位状态告警！";
                    eventWarning.put("url","/image/1.jpg");
                    eventWarning.put("message",content);
                }else {
                    content = deviceName + "监测井盖水位状态复原！";
                    eventWarning.put("url","/image/1.jpg");
                    eventWarning.put("message",content);
                }
                break;
            case 1004:
                if (js.getIntValue("manhole_cover_open_state") == 1){
                    content = deviceName + "监测井盖被打开告警！";
                    eventWarning.put("url","/image/1.jpg");
                    eventWarning.put("message",content);
                }else {
                    content = deviceName + "监测井盖打开后复原！";
                    eventWarning.put("url","/image/1.jpg");
                    eventWarning.put("message",content);
                }
                break;
            default:
                content = deviceName + "监测井盖出现未知事件告警！";
                eventWarning.put("url","/image/1.jpg");
                eventWarning.put("message",content);
        }
        manholeEventService.write(deviceName,content,time);
    }

    public Date stampToDate(String timestamp) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long ts = Long.parseLong(timestamp);
        return sdf.parse(sdf.format(ts));
    }

    @ApiOperation(value = "将最近100条告警数据反馈给前端")
    @CrossOrigin
    @GetMapping("/showEvent")
    public List<ManholeEvent> showEvent(){
        return manholeEventService.findRecent();
    }

    @ApiOperation(value = "根据设备名称对上报事件进行搜索")
    @CrossOrigin
    @GetMapping("/findEvent")
    public List<ManholeEvent> findEvent(String deviceName){
        return manholeEventService.findByName(deviceName);
    }

    @ApiOperation(value = "进行数据的实时告警")
    @CrossOrigin
    @GetMapping("/eventWarn")
    public JSONObject eventWarn(){
        JSONObject result = new JSONObject();
        if(eventWarning.equals(new JSONObject())){
            return eventWarning;
        }
        result = eventWarning;
        eventWarning = new JSONObject();
        return result;
    }

    @ApiOperation(value = "展示井盖设备数据列表、经纬度数据")
    @CrossOrigin
    @GetMapping("/showReport")
    public List<ManholeReport> showReport(){
        List<ManholeReport> manholeReports = manholeReportService.find();
        JSONArray ja = new JSONArray();
        int i=0;
        String url = "http://localhost:8081/api/device/ip/localhost";
        try {
            ja = restTemplate.getForObject(url,JSONArray.class);
        }catch (Exception ignored){}
        for (ManholeReport manholeReport : manholeReports){
            for (i=0; i<ja.size(); i++){
                if (manholeReport.getDeviceID().equals(ja.getJSONObject(i).getString("id"))){
                    break;
                }
            }
            if (i==ja.size()){
                manholeReportService.remove(manholeReport.getDeviceID());
            }
        }
        return manholeReportService.find();
    }

    @ApiOperation(value = "通过设备名称搜索设备列表信息")
    @CrossOrigin
    @GetMapping("/findReport")
    public ManholeReport findReport(String deviceName){
        return manholeReportService.findByName(deviceName);
    }
}
