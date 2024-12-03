package com.tsinghua.tagsystem.queue;
import com.tsinghua.tagsystem.model.params.RunTestModelParam;
import org.springframework.stereotype.Component;
import java.util.LinkedList;
import java.util.Queue;

@Component
public class MessageQueue {

    private final Queue<RunTestModelParam> queue;

    // 构造函数，初始化队列
    public MessageQueue() {
        this.queue = new LinkedList<>();
    }

    // 向队列中添加消息
    public synchronized void enqueue(RunTestModelParam message) {
        queue.offer(message);  // 将任务添加到队列
        System.out.println("Message added: " + message);
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

