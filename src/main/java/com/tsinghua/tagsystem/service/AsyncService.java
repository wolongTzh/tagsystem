package com.tsinghua.tagsystem.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public interface AsyncService {
   @Async
   public void asyncTrain();
}
