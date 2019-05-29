package com.example.demo;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class current {
    //请求总数
    public static int clientTotal=5000;
    //并发数
    public static int threadTotal=200;

//    public static int count=0;
    public static AtomicInteger count=new AtomicInteger(0);

    static Logger log= LoggerFactory.getLogger(current.class);

    public static void main(String[] args) throws InterruptedException {
        //定义线程池
        ExecutorService executorService= Executors.newCachedThreadPool();
        final Semaphore semaphore=new Semaphore(threadTotal);
        final CountDownLatch countDownLatch=new CountDownLatch(clientTotal);
        for (int i=0;i<clientTotal;i++){
            executorService.execute(()->{
                try {
                    semaphore.acquire();
                    add();
                    semaphore.release();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    log.error("exception",e);
                }
                countDownLatch.countDown();
            });
        }
        countDownLatch.await();
        executorService.shutdown();
        log.info("count:{}",count.get());
    }
    private static void  add(){
//        count++;
        count.getAndIncrement();
//        count.incrementAndGet();
    }
}
