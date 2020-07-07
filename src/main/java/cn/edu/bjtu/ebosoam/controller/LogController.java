package cn.edu.bjtu.ebosoam.controller;

import cn.edu.bjtu.ebosoam.entity.Log;
import cn.edu.bjtu.ebosoam.service.LogService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
    public List<Log> getLog(Date firstDate, Date lastDate, String source, String category, String operation) throws ParseException {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat ds = new SimpleDateFormat("yyyy-MM-dd ");
        Date startDate = df.parse(ds.format(firstDate) + "00:00:00");
        Date endDate = df.parse(ds.format(lastDate) + "23:59:59");
        return logService.find(startDate, endDate, source, category, operation);
    }
}
