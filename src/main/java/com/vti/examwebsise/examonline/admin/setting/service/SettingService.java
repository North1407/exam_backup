package com.vti.examwebsise.examonline.admin.setting.service;

import com.vti.examwebsise.examonline.admin.setting.repo.SettingRepo;
import com.vti.examwebsise.examonline.common.entity.Setting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SettingService {
    @Autowired
    SettingRepo repo;

    public List<Setting> getAllSettings() {
        return repo.findAll();
    }

    public void saveAllSettings(String[] ids, String[] values) {
        for (int i = 0; i < ids.length; i++) {
            if("".equals(values[i])){
                continue;
            }
            Setting setting = repo.findById(Integer.parseInt(ids[i])).orElseThrow();
            setting.setValue(Integer.parseInt(values[i]));
            repo.save(setting);
        }
    }

    public int getExamTime() {
        return repo.findByName("Time per exam");
    }

    public int getNumberOfQuestion() {
        return repo.findByName("Question per exam");
    }
}
