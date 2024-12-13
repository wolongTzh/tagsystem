package com.tsinghua.tagsystem.queue;
import com.tsinghua.tagsystem.model.params.RunTestModelParam;
import com.tsinghua.tagsystem.model.params.StartLLMTaskParam;
import com.tsinghua.tagsystem.service.EvalDetailService;
import com.tsinghua.tagsystem.service.LLMTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class TaskProcessor {

    @Autowired
    EvalDetailService evalDetailService;

    @Autowired
    LLMTaskService llmTaskService;

    private final ExecutorService executorService;  // 线程池
    private final MessageQueue messageQueue;  // 任务队列

    // 构造函数，注入任务队列
    @Autowired
    public TaskProcessor(MessageQueue messageQueue) {
        this.messageQueue = messageQueue;
        this.executorService = Executors.newFixedThreadPool(1);  // 固定线程池大小为 2

        // 启动后台线程来处理任务
        for (int i = 0; i < 1; i++) {
            executorService.submit(this::processQueue);  // 每个线程执行 processQueue 方法
        }
    }

    // 从队列中持续处理任务
    private void processQueue() {
        while (true) {
            RunTestModelParam message = null;

            synchronized (messageQueue) {
                if (!messageQueue.isEmpty()) {
                    message = messageQueue.dequeue();  // 从队列中获取并移除任务
                }
            }

            if (message != null) {
                System.out.println("Processing message: " + message);
                try {
                    if (message.getTaskType().equals("customize")) {
                        evalDetailService.runTest(message);
                    }
                    else if (message.getTaskType().equals("promote")) {
                        evalDetailService.runTestPromote(message);
                    }
                    else if (message.getTaskType().equals("compare")) {
                        evalDetailService.runTestHug(message);
                    }
                    else if (message.getTaskType().equals("train")) {
                        evalDetailService.runTrain(message);
                    }
                    else if (message.getTaskType().equals("LLM")) {
                        llmTaskService.runTask(StartLLMTaskParam.builder()
                                .evalOverviewId(message.getEvalOverviewId())
                                .llmTaskId(message.getModelId())
                                .build());
                    }
                    else {
                        System.out.println("no match task type!!");
                    }
                }
                catch (IOException e) {
                    System.out.println("Error occurred while processing message: " + e);
                }
                // 这里可以加上任务处理的具体逻辑
            } else {
                try {
                    // 如果队列为空，当前线程休眠一段时间再继续检查队列
                    Thread.sleep(100);  // 这里可以调整休眠时间来优化性能
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();  // 如果线程被中断，则停止
                    break;
                }
            }
        }
    }

    // 停止线程池
    public void stopProcessing() {
        executorService.shutdown();
    }
}
