package cn.edu.bjtu.ebosoam.controller;

import cn.edu.bjtu.ebosoam.entity.Log;
import cn.edu.bjtu.ebosoam.model.LocalLog;
import cn.edu.bjtu.ebosoam.service.LogService;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@RequestMapping("/api")
@RestController
public class LogController {
    @Autowired
    LogService logService;
    @Autowired
    RestTemplate restTemplate;

    @ApiOperation(value = "按条件筛选日志")
    @CrossOrigin
    @RequestMapping(value = "/log", method = RequestMethod.GET)
    public List<LocalLog> getLogFromAllGateways(Date firstDate, Date lastDate, String source, String category, String operation) throws ParseException {
        List<LocalLog> res = new LinkedList<>();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat ds = new SimpleDateFormat("yyyy-MM-dd ");
        Date startDate = df.parse(ds.format(firstDate) + "00:00:00");
        Date endDate = df.parse(ds.format(lastDate) + "23:59:59");
        List<Log> logs = logService.find(startDate, endDate, source, category, operation);
        for (Log log : logs) {
            res.add(new LocalLog(log.getDate(), log.getSource(), log.getCategory(), log.getOperation(), log.getMessage(), "配置中心本地"));
        }
        JSONArray gatewayInfoList = restTemplate.getForObject("http://localhost:8089/api/gateway", JSONArray.class);
        System.out.println(gatewayInfoList);
        for (int i = 0; i < gatewayInfoList.size(); i++) {
            JSONObject jsonObject = gatewayInfoList.getJSONObject(i);
            try {
                @SuppressWarnings("unchecked")
                List<Log> logList = restTemplate.getForObject("http://" + jsonObject.getString("ip")
                        + ":8090/api/log?firstDate=" + firstDate
                        + "&lastDate=" + lastDate
                        + "&source=" + source
                        + "&category=" + category
                        + "&operation=" + operation, List.class);
                for (Log log : logList) {
                    res.add(new LocalLog(log.getDate(), log.getSource(), log.getCategory(), log.getOperation(), log.getMessage(), jsonObject.getString("name")));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return res;
    }
}
