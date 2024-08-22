package com.tsinghua.tagsystem.service.impl;

import com.tsinghua.tagsystem.service.ModelService;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Service
public class ModelServiceImpl implements ModelService {

    @Override
    public void train() {
        try{
            String cmd = "cd /home/tz/copy-code/W2NER-main && /home/tz/miniconda3/envs/pufa/bin/python3.8 predict_for_test.py'";
            Process process = Runtime.getRuntime().exec(cmd);
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            reader.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
