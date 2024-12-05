package com.tsinghua.tagsystem.queue;
import com.tsinghua.tagsystem.dao.entity.EvalDetail;
import com.tsinghua.tagsystem.dao.entity.ModelInfo;
import com.tsinghua.tagsystem.dao.mapper.ModelInfoMapper;
import com.tsinghua.tagsystem.model.params.RunTestModelParam;
import com.tsinghua.tagsystem.service.EvalDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.LinkedList;
import java.util.Queue;

@Component
public class MessageQueue {

    @Autowired
    EvalDetailService evalDetailService;

    @Autowired
    ModelInfoMapper modelInfoMapper;

    private final Queue<RunTestModelParam> queue;

    // 构造函数，初始化队列
    public MessageQueue() {
        this.queue = new LinkedList<>();
    }

    // 向队列中添加消息
    public synchronized int enqueue(RunTestModelParam message) {
        int retId = 0;
        if(message.getTaskType().equals("train")) {
            ModelInfo modelInfo = modelInfoMapper.selectById(message.getModelId());
            modelInfo.setStatus("排队中");
            modelInfoMapper.updateById(modelInfo);
            retId = message.getModelId();
        }
        else {
            int evalDetailId = evalDetailService.addNewScore(EvalDetail.builder()
                    .evalDataName(message.getEvalDataName())
                    .evalDataId(message.getEvalDataId())
                    .evalOverviewId(message.getEvalOverviewId())
                    .modelId(message.getModelId())
                    .modelName(message.getModelName())
                    .evalUserId(message.getEvalUserId())
                    .evalUserName(message.getEvalUserName())
                    .evalScore("排队中")
                    .build());
            message.setEvalDetailId(evalDetailId);
            retId = evalDetailId;
        }
        queue.offer(message);  // 将任务添加到队列
        System.out.println("Message added: " + message);
        return retId;
    }

    // 获取并移除队列中的消息
    public synchronized RunTestModelParam dequeue() {
        return queue.poll();  // 从队列中获取并移除任务
    }

    // 获取队列的大小
    public synchronized int size() {
        return queue.size();
    }

    // 判断队列是否为空
    public synchronized boolean isEmpty() {
        return queue.isEmpty();
    }
}

