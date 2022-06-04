package org.faqtong.myjobschedulor.config;

import lombok.extern.slf4j.Slf4j;
import org.faqtong.myjobschedulor.mockdata.MockData;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

/**
 * configuration
 * <p>
 * Created on 2022/6/4
 *
 * @author tongw
 */
@Component
@Slf4j
public class MyjobConfig implements InitializingBean, DisposableBean {

    private MockData mockData = null;

    @Override
    public void destroy() throws Exception {

    }

    @Override
    public void afterPropertiesSet() throws Exception {
        mockData = new MockData();
        mockData.init();

    }
}
