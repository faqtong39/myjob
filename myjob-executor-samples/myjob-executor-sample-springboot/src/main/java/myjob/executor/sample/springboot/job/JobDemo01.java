package myjob.executor.sample.springboot.job;

import cn.hutool.core.date.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * job demo
 * <p>
 * Created on 2022/6/4
 *
 * @author tongw
 */
@Component
@Slf4j
public class JobDemo01 implements BaseJob {

    @Override
    public Object execute(Object params) {

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        log.info("***** JobDemo01 with paramters[{}] is running........", params);

        return DateUtil.now();
    }
}
