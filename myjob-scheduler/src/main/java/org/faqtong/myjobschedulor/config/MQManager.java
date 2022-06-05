package org.faqtong.myjobschedulor.config;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import org.faqtong.myjobschedulor.mq.JobEventFactory;
import org.faqtong.myjobschedulor.mq.JobEventHandler;
import org.faqtong.myjobschedulor.mq.JobQueue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Disruptor configuration
 * <p>
 * Created on 2022/6/5
 *
 * @author tongw
 */
@Configuration
public class MQManager {


    @SuppressWarnings({"deprecation", "unchecked"})
    @Bean
    public RingBuffer<JobQueue> MessageVoRingBuffer() {
        ExecutorService executor = Executors.newFixedThreadPool(2);
        JobEventFactory factory = new JobEventFactory();
        int bufferSize = 1024 * 256;

        Disruptor<JobQueue> disruptor = new Disruptor<>(factory, bufferSize, executor,
                ProducerType.SINGLE, new BlockingWaitStrategy());

        disruptor.handleEventsWith(new JobEventHandler());

        disruptor.start();
        RingBuffer<JobQueue> ringBuffer = disruptor.getRingBuffer();

        return ringBuffer;
    }
    
}
