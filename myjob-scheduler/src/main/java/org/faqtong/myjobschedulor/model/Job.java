package org.faqtong.myjobschedulor.model;

import lombok.Data;

/**
 * Job model
 * <p>
 * Created on 2022/6/4
 *
 * @author tongw
 */
@Data
public class Job {
    private Long id;
    private String code;
    private String executorGroupAppName;
    private String jobDesc;
    private String createTime;
    private String updateTime;
    private String author;
    private String alarmEmail;
    private String scheduleType;
    private String scheduleConf;
    private String scheduleExpireStrategy;
    private String executorRouteStrategy;
    private String jobKey;
    private String jobParam;
    private Integer jobTimeout;
    private Integer jobFailRetryCount;
    private String sourceType;
    private Integer triggerStatus;
    private String triggerLastTime;
    private String triggerNextTime;
    private ExecutorGroup executorGroup;
}
