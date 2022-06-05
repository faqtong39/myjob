package org.faqtong.myjobschedulor.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import lombok.extern.slf4j.Slf4j;
import org.faqtong.myjobschedulor.enums.TriggerStatus;
import org.faqtong.myjobschedulor.form.JobForm;
import org.faqtong.myjobschedulor.mockdata.MockData;
import org.faqtong.myjobschedulor.model.ExecutorGroup;
import org.faqtong.myjobschedulor.model.Job;
import org.faqtong.myjobschedulor.utils.JobUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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

    @PostMapping("/start")
    @ResponseBody
    public String start(String code, ModelMap map) {
        MockData.jobMap.get(code).setTriggerStatus(TriggerStatus.Start.value());
        return "SUCCESS";
    }

    @PostMapping("/stop")
    @ResponseBody
    public String stop(String code, ModelMap map) {
        MockData.jobMap.get(code).setTriggerStatus(TriggerStatus.Stop.value());
        return "SUCCESS";
    }

    @PostMapping("/oneTimeExec")
    @ResponseBody
    public String oneTimeExec(String appName, String jobCode, String jobKey, String jobParam, int timeout, int retry, ModelMap map) {
        log.info("jobKey: {}", jobKey);
        log.info("jobParam: {}", jobParam);

        return JobUtil.execute(appName, jobCode, jobKey, jobParam, timeout, retry);
    }

    @GetMapping("/log")
    public String log(ModelMap map) {
        map.addAttribute("logs", MockData.logList);
        return "log";
    }

//    public static void main(String[] args) {
//        String triggerTime = DateUtil.format(DateUtil.date(System.currentTimeMillis()), "yyyy-MM-dd HH:mm:ss.SSS");
//        System.out.println(triggerTime);
//        System.out.println(DateUtil.offsetSecond(DateUtil.parseDateTime(triggerTime), 10));
//        System.out.println(DateUtil.formatDateTime(DateUtil.offsetSecond(DateUtil.parseDateTime(triggerTime), 10)));
//    }
}
