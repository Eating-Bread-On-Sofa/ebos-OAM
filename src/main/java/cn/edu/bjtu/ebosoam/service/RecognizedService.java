package cn.edu.bjtu.ebosoam.service;

import cn.edu.bjtu.ebosoam.entity.Recognized;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface RecognizedService {
    List<Recognized> findAll();
    List<Recognized> findRecent();
    List<Recognized> find(String name);
}
