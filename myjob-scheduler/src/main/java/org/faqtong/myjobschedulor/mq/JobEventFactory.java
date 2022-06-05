package org.faqtong.myjobschedulor.mq;

import com.lmax.disruptor.EventFactory;

/**
 * JobEventFactory
 * <p>
 * Created on 2022/6/5
 *
 * @author tongw
 */
public class JobEventFactory implements EventFactory<JobQueue> {

    @Override
    public JobQueue newInstance() {
        return new JobQueue();
    }
}
