package org.faqtong.myjobschedulor.controller;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.http.HttpRequest;
import lombok.extern.slf4j.Slf4j;
import org.faqtong.myjobschedulor.enums.TriggerStatus;
import org.faqtong.myjobschedulor.form.JobForm;
import org.faqtong.myjobschedulor.mockdata.MockData;
import org.faqtong.myjobschedulor.model.Executor;
import org.faqtong.myjobschedulor.model.ExecutorGroup;
import org.faqtong.myjobschedulor.model.Job;
import org.faqtong.myjobschedulor.model.Log;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * Index
 * <p>
 * Created on 2022/6/3
 *
 * @author tongw
 */
@Controller
@Slf4j
public class PageController {

    @GetMapping("/index")
    public String index() {
        return "index";
    }

    @GetMapping("/executor")
    public String executor(ModelMap map) {
        map.addAttribute("executorGroups", MockData.executorGroupMap);
        return "executor";
    }

    @GetMapping("/job")
    public String job(ModelMap map) {
        map.addAttribute("executorGroups", MockData.executorGroupMap);
        map.addAttribute("jobs", MockData.jobMap);
        return "job";
    }

    @PostMapping("/jobSubmit")
    public String jobSubmit(JobForm form, ModelMap map) {
        Job job = new Job();

        job.setCode(IdUtil.simpleUUID());
        job.setExecutorGroupAppName(form.getExecutor());
        job.setJobDesc(form.getJobDesc());
        job.setAuthor(form.getAuthor());
        job.setAlarmEmail(form.getEmail());
        job.setScheduleType(form.getScheduleType());
        job.setScheduleConf(form.getCronValue());
        job.setSourceType(form.getSourceType());
        job.setJobKey(form.getJobKey());
        job.setJobParam(form.getJobParam());
        job.setExecutorRouteStrategy(form.getRouteStrategy());
        job.setScheduleExpireStrategy(form.getExpireStrategy());
        job.setTriggerStatus(TriggerStatus.Stop.value());
        job.setJobTimeout(form.getTimeout());
        job.setJobFailRetryCount(form.getRetry());
        job.setCreateTime(DateUtil.now());
        job.setUpdateTime(job.getCreateTime());

        ExecutorGroup executorGroup = MockData.executorGroupMap.get(job.getExecutorGroupAppName());
        job.setExecutorGroup(executorGroup);

        MockData.jobMap.put(job.getCode(), job);

        map.addAttribute("jobs", MockData.jobMap);

        return "job";
    }

    @PostMapping("/oneTimeExec")
    @ResponseBody
    public String oneTimeExec(String appName, String jobKey, String jobParam, int timeout, int retry, ModelMap map) {
        log.info("jobKey: {}", jobKey);
        log.info("jobParam: {}", jobParam);
        ExecutorGroup executorGroup = MockData.executorGroupMap.get(appName);
        // TODO: select one executor depends on route strategy
        Executor executor = executorGroup.getExecutorList().get(0);

        // //request template: jobKey|jobParam|timeout|retryTimes
        String request = jobKey + "|" + jobParam;

        log.info("job execution request: {}", request);
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("request", Base64.encode(request));

        //TODO: implement timeout and retry logic simply here
        String result = "";
        int execCnt = 0;
        String triggerTime = DateUtil.format(DateUtil.date(System.currentTimeMillis()), "yyyy-MM-dd HH:mm:ss.SSS");
        for (int i = 0; i < retry; i++) {
            execCnt = i + 1;
            try {
                result = HttpRequest.post("http://" + executor.getAddress() + "/myjob")
                        .form(paramMap)
                        .timeout(timeout * 1000)
                        .execute().body();
            } catch (Exception e) {
                Log log = new Log();
                log.setJobCode(jobKey);
                log.setExecutorGroupCode(executorGroup.getAppName());
                log.setTriggerTime(triggerTime);
                log.setTriggerCode("Success");
                log.setTriggerMsg(jobKey + " is triggered " + execCnt + " times");
                log.setExecutionTime(DateUtil.format(DateUtil.date(System.currentTimeMillis()), "yyyy-MM-dd HH:mm:ss.SSS"));
                log.setExecutionCode("Fail");
                log.setExecutionMsg(e.getMessage());
                MockData.logList.add(log);

                result = "FAIL";
            }

            if (result.toUpperCase().contains("SUCCESS")) {
                break;
            } else {
                //TODO: Alarm mail logic
            }
        }

        // log here
        Log log = new Log();
        log.setJobCode(jobKey);
        log.setExecutorGroupCode(executorGroup.getAppName());
        log.setTriggerTime(triggerTime);
        log.setTriggerCode("Success");
        log.setTriggerMsg("Triggered " + execCnt + " times");
        log.setExecutionTime(DateUtil.format(DateUtil.date(System.currentTimeMillis()), "yyyy-MM-dd HH:mm:ss.SSS"));
        log.setExecutionCode("Success");
        log.setExecutionMsg("Success");
        MockData.logList.add(log);

        return result;
    }

    @GetMapping("/log")
    public String log(ModelMap map) {
        map.addAttribute("logs", MockData.logList);
        return "log";
    }


}
