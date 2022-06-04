package org.faqtong.myjobschedulor.controller;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import lombok.extern.slf4j.Slf4j;
import org.faqtong.myjobschedulor.enums.RegistryType;
import org.faqtong.myjobschedulor.enums.Result;
import org.faqtong.myjobschedulor.mockdata.MockData;
import org.faqtong.myjobschedulor.model.Executor;
import org.faqtong.myjobschedulor.model.ExecutorGroup;
import org.faqtong.myjobschedulor.model.Log;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * Restful Api
 * <p>
 * Created on 2022/6/4
 *
 * @author tongw
 */
@RestController
@RequestMapping("/api")
@Slf4j
public class ApiController {

    @PostMapping("/registry")
    public String registry(String request) { // request template: app_name|address
        Assert.notNull(request, "Registry request is required");
        request = Base64.decodeStr(request);
        String[] array = request.split("\\|");
        if (MockData.executorGroupMap.get(array[0]) == null) {
            ExecutorGroup executorGroup = new ExecutorGroup();
            Executor executor = new Executor();

            executorGroup.setCode(IdUtil.simpleUUID());
            executorGroup.setAppName(array[0]);
            executorGroup.setTitle(executorGroup.getAppName());
            executorGroup.setRegistryType(RegistryType.Auto.value());
            executorGroup.setUpdateTime(DateUtil.now());

            executor.setAddress(array[1]);
            executor.setExecutorGroupCode(executorGroup.getCode());
            executor.setUpdateTime(executorGroup.getUpdateTime());

            List<Executor> executorList = new ArrayList<>();
            executorList.add(executor);

            executorGroup.setExecutorList(executorList);

            MockData.executorGroupMap.put(executorGroup.getAppName(), executorGroup);
        } else {
            Executor executor = new Executor();
            executor.setAddress(array[1]);
            executor.setExecutorGroupCode(MockData.executorGroupMap.get(array[0]).getCode());
            executor.setUpdateTime(DateUtil.now());

            MockData.executorGroupMap.get(array[0]).getExecutorList().add(executor);
        }

        log.info("Executor groups: {}", MockData.executorGroupMap);

        return Result.Success.value();
    }

    @PostMapping("/executionLog")
    public String executionLog(String request) { // request template: executor_group_code|job_code|trigger_time|execution_time|execution_code|execution_msg
        Assert.notNull(request, "Execution log request is required");
        request = Base64.decodeStr(request);
        String[] array = request.split("\\|");
        Log log = MockData.logMap.get(array[0] + "." + array[1] + "." + array[2]);
        log.setExecutionTime(array[3]);
        log.setExecutionCode(array[4]);
        log.setExecutionMsg(array[5]);

        return log.toString();
    }
}
