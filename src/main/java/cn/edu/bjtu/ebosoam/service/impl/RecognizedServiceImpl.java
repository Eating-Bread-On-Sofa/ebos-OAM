package cn.edu.bjtu.ebosoam.service.impl;

import cn.edu.bjtu.ebosoam.entity.Recognized;
import cn.edu.bjtu.ebosoam.service.RecognizedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecognizedServiceImpl implements RecognizedService {

    @Autowired
    MongoTemplate mongoTemplate;

    @Override
    public List<Recognized> findAll() {
        return mongoTemplate.findAll(Recognized.class,"recognized_name");
    }

    @Override
    public List<Recognized> findRecent() {
        Query query = new Query();
        query.limit(100);
        return mongoTemplate.find(query,Recognized.class,"recognized_name");
    }

    @Override
    public List<Recognized> find(String name) {
        Query query = Query.query(Criteria.where("name").is(name));
        return mongoTemplate.find(query,Recognized.class,"recognized_name");
    }
}
