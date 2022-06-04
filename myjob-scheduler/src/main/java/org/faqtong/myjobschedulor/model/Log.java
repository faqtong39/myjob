package org.faqtong.myjobschedulor.model;

import lombok.Data;

/**
 * Log model
 * <p>
 * Created on 2022/6/4
 *
 * @author tongw
 */
@Data
public class Log {
    private Long id;
    private String executorGroupCode;
    private String jobCode;
    private String triggerTime;
    private String triggerCode;
    private String triggerMsg;
    private String executionTime;
    private String executionCode;
    private String executionMsg;
    private Integer alarmStatus;
}
