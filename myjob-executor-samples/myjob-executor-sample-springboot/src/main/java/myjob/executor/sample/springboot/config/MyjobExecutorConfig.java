package myjob.executor.sample.springboot.config;

import cn.hutool.core.codec.Base64;
import cn.hutool.http.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

/**
 * configuration
 * <p>
 * Created on 2022/6/4
 *
 * @author tongw
 */
@Component
@Slf4j
public class MyjobExecutorConfig implements InitializingBean, DisposableBean {

    @Value("${myjob.scheduler.endpoint}")
    private String myjobSchedulorEndpoint;

    @Value("${myjob.scheduler.protocal}")
    private String myjobSchedulorProtocal;

    @Value("${spring.application.name}")
    private String appName;

    @Value("${server.port}")
    private Integer port;

    @Override
    public void destroy() throws Exception {

    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if ("http".equals(myjobSchedulorProtocal)) {
            String ip = null;
            try {
                ip = InetAddress.getLocalHost().getHostAddress();
            } catch (UnknownHostException e) {
                log.error(String.format("获取宿主机IP地址失败:{}", e.getMessage()));
            }

            String request = appName + "|" + ip + ":" + port;
            log.info("Registry info: {}", request);
            String registryRequest = Base64.encode(request);
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("request", registryRequest);

            String result = HttpUtil.post(myjobSchedulorProtocal + "://" + myjobSchedulorEndpoint + "/api/registry", paramMap);
            log.info("Registry {} ...", result);

        } else {
            log.error("Only support http protocal");
        }

    }
}
