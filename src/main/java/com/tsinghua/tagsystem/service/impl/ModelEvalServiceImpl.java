package com.tsinghua.tagsystem.service.impl;

import com.tsinghua.tagsystem.dao.entity.ModelEval;
import com.tsinghua.tagsystem.manager.ModelEvalManager;
import com.tsinghua.tagsystem.service.ModelEvalService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
public class ModelEvalServiceImpl implements ModelEvalService {

    @Autowired
    ModelEvalManager modelEvalManager;


    @Override
    public void createModelEval(ModelEval modelEval) {
        LocalDateTime localDateTime = LocalDateTime.now();
        modelEval.setModelEvalTime(localDateTime);
        modelEvalManager.saveOrUpdate(modelEval);
    }
}
