package cn.edu.bjtu.ebosoam.service;

import cn.edu.bjtu.ebosoam.entity.ManholeEvent;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Create by ZhiYuan
 * data:2021/5/6
 */

@Service
public interface ManholeEventService {
    void write(String deviceName, String eventContent, Date time);
    List<ManholeEvent> findRecent();
    List<ManholeEvent> findByName(String deviceName);

}
