package cn.edu.bjtu.ebosoam.service.impl;

import cn.edu.bjtu.ebosoam.entity.ManholeEvent;
import cn.edu.bjtu.ebosoam.service.ManholeEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Create by ZhiYuan
 * data:2021/5/6
 */

@Service
public class ManholeEventImpl implements ManholeEventService {

    @Autowired
    MongoTemplate mongoTemplate;

    @Override
    public void write(String deviceName, String eventContent, Date time) {
        ManholeEvent manholeEvent = new ManholeEvent();
        manholeEvent.setDeviceName(deviceName);
        manholeEvent.setEventContent(eventContent);
        manholeEvent.setTime(time);
        mongoTemplate.save(manholeEvent);
    }

    @Override
    public List<ManholeEvent> findRecent() {
        Query query = new Query();
        query.with(Sort.by(Sort.Order.desc("time"))).limit(100);
        return mongoTemplate.find(query, ManholeEvent.class,"manhole_event");
    }

    @Override
    public List<ManholeEvent> findByName(String deviceName) {
        Query query = Query.query(Criteria.where("deviceName").is(deviceName));
        query.with(Sort.by(Sort.Order.desc("time"))).limit(100);
        return mongoTemplate.find(query, ManholeEvent.class,"manhole_event");
    }
}
