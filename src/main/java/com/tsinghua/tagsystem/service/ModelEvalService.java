package com.tsinghua.tagsystem.service;

import com.tsinghua.tagsystem.dao.entity.ModelEval;
import org.springframework.stereotype.Service;

@Service
public interface ModelEvalService {
    public void createModelEval(ModelEval modelEval);
}
