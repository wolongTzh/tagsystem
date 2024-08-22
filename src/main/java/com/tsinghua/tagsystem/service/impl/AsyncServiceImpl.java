package com.tsinghua.tagsystem.service.impl;

import com.tsinghua.tagsystem.service.AsyncService;
import com.tsinghua.tagsystem.service.ModelService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;

@Service
public class AsyncServiceImpl implements AsyncService {

    @Override
    @Async
    public void asyncTrain() {
        try {
            String cmd = "sh cmd.sh";
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
