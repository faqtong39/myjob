package org.faqtong.myjobschedulor.form;

import lombok.Data;

/**
 * job form
 * <p>
 * Created on 2022/6/4
 *
 * @author tongw
 */
@Data
public class JobForm {
    private String executor;
    private String jobDesc;
    private String author;
    private String email;
    private String scheduleType;
    private String cronValue;
    private String sourceType;
    private String jobKey;
    private String jobParam;
    private String routeStrategy;
    private String expireStrategy;
    private Integer timeout;
    private Integer retry;

}
