package org.faqtong.myjobschedulor.model;

import lombok.Data;

/**
 * Executor model
 * <p>
 * Created on 2022/6/4
 *
 * @author tongw
 */
@Data
public class Executor {
    private Long id;
    private String executorGroupCode;
    private String address;
    private String updateTime;
}
