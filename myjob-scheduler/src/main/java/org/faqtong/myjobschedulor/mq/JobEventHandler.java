package org.faqtong.myjobschedulor.mq;

import com.lmax.disruptor.EventHandler;
import lombok.extern.slf4j.Slf4j;
import org.faqtong.myjobschedulor.model.Job;
import org.faqtong.myjobschedulor.utils.JobUtil;

/**
 * consumer
 * <p>
 * Created on 2022/6/5
 *
 * @author tongw
 */
@Slf4j
public class JobEventHandler implements EventHandler<JobQueue> {

    @Override
    public void onEvent(JobQueue jobQueue, long l, boolean b) throws Exception {
        log.info("job consumer {} on {}...", jobQueue.getJob().getJobKey(), jobQueue.getJob().getTriggerLastTime());
        Job job = jobQueue.getJob();
        JobUtil.execute(job.getExecutorGroupAppName(), job.getCode(), job.getJobKey(), job.getJobParam(), job.getJobTimeout(), job.getJobFailRetryCount());
    }
}
