package org.faqtong.myjobschedulor.utils;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HttpRequest;
import lombok.extern.slf4j.Slf4j;
import org.faqtong.myjobschedulor.mockdata.MockData;
import org.faqtong.myjobschedulor.model.Executor;
import org.faqtong.myjobschedulor.model.ExecutorGroup;
import org.faqtong.myjobschedulor.model.Job;
import org.faqtong.myjobschedulor.model.Log;

import java.util.HashMap;
import java.util.Map;

/**
 * job util
 * <p>
 * Created on 2022/6/5
 *
 * @author tongw
 */
@Slf4j
public class JobUtil {

    public static String execute(String appName, String jobCode, String jobKey, String jobParam, int timeout, int retry) {
        ExecutorGroup executorGroup = MockData.executorGroupMap.get(appName);
        // TODO: select one executor depends on route strategy, simple random
        Executor executor = executorGroup.getExecutorList().get(RandomUtil.randomInt(0,
                executorGroup.getExecutorList().size()));

        // //request template: jobKey|jobParam
        String request = jobKey + "|" + jobParam;

        log.info("job execution request: {}", request);
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("request", Base64.encode(request));

        //TODO: implement timeout and retry logic simply here
        String result = "";
        String triggerTime = null;
        int execCnt = 0;
        for (int i = 0; i < retry; i++) {
            execCnt = i + 1;
            triggerTime = DateUtil.format(DateUtil.date(System.currentTimeMillis()), "yyyy-MM-dd HH:mm:ss.SSS");
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
                Job job = MockData.jobMap.get(jobCode);
                job.setTriggerLastTime(triggerTime);
                if ("0".equals(job.getScheduleType())) {
                    job.setTriggerNextTime(DateUtil.formatDateTime(DateUtil.offsetSecond(
                            DateUtil.parseDateTime(job.getTriggerLastTime()), Integer.valueOf(job.getScheduleConf()))));
                } else {
                    //TODO: cron
                }

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
}
