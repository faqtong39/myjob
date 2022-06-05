package org.faqtong.myjobschedulor.config;

import cn.hutool.core.date.DateUtil;
import com.lmax.disruptor.RingBuffer;
import io.micrometer.core.instrument.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.faqtong.myjobschedulor.enums.TriggerStatus;
import org.faqtong.myjobschedulor.mockdata.MockData;
import org.faqtong.myjobschedulor.model.Job;
import org.faqtong.myjobschedulor.mq.JobQueue;
import org.faqtong.myjobschedulor.utils.SpringContextUtils;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;

/**
 * configuration
 * <p>
 * Created on 2022/6/4
 *
 * @author tongw
 */
@Component
@Slf4j
public class MyjobConfig implements ApplicationRunner {

    private MockData mockData = null;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        mockData = new MockData();
        mockData.init();
        JobThread jobThread = new JobThread();
        jobThread.start();
    }

    class JobThread extends Thread {
        private RingBuffer<JobQueue> ringBuffer = SpringContextUtils.getBean(RingBuffer.class);

        @Override
        public void run() {
            // TODO: just simple logic here
            while (true) {
                Map<String, Job> jobMap = MockData.jobMap;
                if (jobMap != null) {
                    Collection<Job> values = jobMap.values();
                    for (Job job : values) {
                        if (job.getTriggerStatus() == TriggerStatus.Start.value()) {

                            if ("0".equals(job.getScheduleType())) {
                                if (StringUtils.isBlank(job.getTriggerLastTime())) {
                                    job.setTriggerLastTime(DateUtil.now());
                                }
                                if (DateUtil.compare(DateUtil.parseDateTime(DateUtil.now()),
                                        DateUtil.offsetSecond(DateUtil.parseDateTime(job.getTriggerLastTime()), Integer.valueOf(job.getScheduleConf()))) > -1) {
                                    log.info("notify executor run {}...", job.getJobKey());
                                    long seq = ringBuffer.next();
                                    try {
                                        job.setTriggerLastTime(
                                                DateUtil.formatDateTime(
                                                        DateUtil.offsetSecond(DateUtil.parseDateTime(job.getTriggerLastTime()), Integer.valueOf(job.getScheduleConf()))));
                                        log.info("trigger last time {}", job.getTriggerLastTime());
                                        job.setTriggerNextTime(
                                                DateUtil.formatDateTime(
                                                        DateUtil.offsetSecond(DateUtil.parseDateTime(job.getTriggerLastTime()), Integer.valueOf(job.getScheduleConf()))));
                                        log.info("trigger next time {}", job.getTriggerNextTime());
                                        JobQueue jobQueue = ringBuffer.get(seq);
                                        jobQueue.setJob(job);
                                    } finally {
                                        ringBuffer.publish(seq);
                                    }
                                }

                            } else {
                                //TODO: cron
                            }


                        }
                    }
                }

            }

        }
    }
}
