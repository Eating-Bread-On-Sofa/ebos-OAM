package cn.edu.bjtu.ebosoam.service;

import cn.edu.bjtu.ebosoam.entity.ManholeReport;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Create by ZhiYuan
 * data:2021/5/10
 */

@Service
public interface ManholeReportService {
    void write(ManholeReport manholeReport);
    List<ManholeReport> find();
    ManholeReport findByName(String deviceName);
    void remove(String deviceID);
}
