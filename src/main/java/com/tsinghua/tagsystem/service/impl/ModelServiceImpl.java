package com.tsinghua.tagsystem.service.impl;

import com.tsinghua.tagsystem.service.AsyncService;
import com.tsinghua.tagsystem.service.ModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;

@Service
public class ModelServiceImpl implements ModelService {
    @Autowired
    AsyncService asyncService;

    @Override
    public void train() {
        asyncService.asyncTrain();
        System.out.println("算法开始训练中，请稍后");
    }
}
