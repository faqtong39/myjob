package org.faqtong.myjobschedulor.model;

import lombok.Data;

import java.util.List;

/**
 * Executor group model
 * <p>
 * Created on 2022/6/4
 *
 * @author tongw
 */
@Data
public class ExecutorGroup {
    private Long id;
    private String code;
    private String appName;
    private String title;
    private Integer registryType;
    private String updateTime;
    private List<Executor> executorList;
}
