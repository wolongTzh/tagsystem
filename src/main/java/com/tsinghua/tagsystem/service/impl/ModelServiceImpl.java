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
            String[] cmd = {"cmd", "/c", "echo Hello, World!"};
            Process process = Runtime.getRuntime().exec("echo Hello, World!");
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
