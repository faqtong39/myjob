package org.faqtong.myjobschedulor.mq;

import lombok.Data;
import org.faqtong.myjobschedulor.model.Job;

/**
 * job message
 * <p>
 * Created on 2022/6/5
 *
 * @author tongw
 */
@Data
public class JobQueue {
    private Job job;
}
