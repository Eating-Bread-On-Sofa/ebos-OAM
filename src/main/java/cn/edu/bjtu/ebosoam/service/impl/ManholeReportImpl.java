package cn.edu.bjtu.ebosoam.service.impl;

import cn.edu.bjtu.ebosoam.entity.ManholeReport;
import cn.edu.bjtu.ebosoam.service.ManholeReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Create by ZhiYuan
 * data:2021/5/10
 */

@Service
public class ManholeReportImpl implements ManholeReportService {

    @Autowired
    MongoTemplate mongoTemplate;

    @Override
    public void write(ManholeReport manholeReport) {
        ManholeReport manhole = new ManholeReport();
        manhole.setDeviceID(manholeReport.getDeviceID());
        manhole.setDeviceName(manholeReport.getDeviceName());
        manhole.setBattery_value(manholeReport.getBattery_value());
        manhole.setRssi(manholeReport.getRssi());
        manhole.setLongitude(manholeReport.getLongitude());
        manhole.setLatitude(manholeReport.getLatitude());
        mongoTemplate.save(manhole);
    }

    @Override
    public List<ManholeReport> find() {
        return mongoTemplate.findAll(ManholeReport.class,"manhole_report");
    }

    @Override
    public ManholeReport findByName(String deviceName) {
        Query query = Query.query(Criteria.where("deviceName").is(deviceName));
        return mongoTemplate.findOne(query, ManholeReport.class,"manhole_report");
    }

    @Override
    public void remove(String deviceID) {
        Query query = Query.query(Criteria.where("deviceID").is(deviceID));
        mongoTemplate.remove(query,ManholeReport.class,"manhole_report");
    }
}
