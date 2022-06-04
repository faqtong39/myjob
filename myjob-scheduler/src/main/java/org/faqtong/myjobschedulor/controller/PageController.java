package org.faqtong.myjobschedulor.controller;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.http.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.faqtong.myjobschedulor.enums.TriggerStatus;
import org.faqtong.myjobschedulor.form.JobForm;
import org.faqtong.myjobschedulor.mockdata.MockData;
import org.faqtong.myjobschedulor.model.Executor;
import org.faqtong.myjobschedulor.model.ExecutorGroup;
import org.faqtong.myjobschedulor.model.Job;
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
    public String oneTimeExec(String appName, String jobKey, String jobParam, String timeout, String retry, ModelMap map) {
        log.info("jobKey: {}", jobKey);
        log.info("jobParam: {}", jobParam);
        ExecutorGroup executorGroup = MockData.executorGroupMap.get(appName);
        // TODO: select one executor depends on route strategy
        Executor executor = executorGroup.getExecutorList().get(0);

        // //request template: jobKey|jobParam|timeout|retryTimes
        String request = jobKey + "|" + jobParam + "|" + timeout + "|" + retry;

        log.info("job execution request: {}", request);
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("request", Base64.encode(request));

        String result = HttpUtil.post("http://" + executor.getAddress() + "/myjob", paramMap);

        // TODO: log here

        return result;
    }

    @GetMapping("/log")
    public String log(ModelMap map) {
        return "log";
    }
}
