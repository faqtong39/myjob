package myjob.executor.sample.springboot.controller;

import cn.hutool.core.codec.Base64;
import lombok.extern.slf4j.Slf4j;
import myjob.executor.sample.springboot.job.BaseJob;
import myjob.executor.sample.springboot.utils.SpringContextUtils;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * job controller
 * <p>
 * Created on 2022/6/4
 *
 * @author tongw
 */
@RestController
@RequestMapping("/myjob")
@Slf4j
public class JobController {

    @PostMapping
    public String executeJob(String request) {//request template: jobKey|jobParam
        Assert.notNull(request, "Execution request is required");
        request = Base64.decodeStr(request);
        log.info(request);
        String[] array = request.split("\\|");
        BaseJob job = (BaseJob) SpringContextUtils.getBean(array[0]);
        String param = array.length == 2 ? array[1] : null;
        return (String) job.execute(param);
    }
}
