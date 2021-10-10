package com.wenxianm.config;

import com.xxl.job.core.executor.impl.XxlJobSpringExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * xxl配置
 * @ClassName XxlJobConfiguration
 * @Author cwx
 * @Date 2021/9/26 11:13
 **/
@Configuration
@Slf4j
public class XxlJobConfiguration {

    /**
     * 调度中心部署地址
     */
    @Value("${xxl.job.admin.addresses}")
    private String adminAddresses;

    /**
     * 执行器appName
     */
    @Value("${xxl.job.executor.appname}")
    private String appName;

    /**
     * 执行器ip地址
     */
    @Value("${xxl.job.executor.ip}")
    private String ip;

    /**
     * 执行器端口
     */
    @Value("${xxl.job.executor.port}")
    private int port;

    /**
     * 执行器通信token
     */
    @Value("${xxl.job.accessToken}")
    private String accessToken;

    /**
     * 执行器日志文件路径
     */
    @Value("${xxl.job.executor.logpath}")
    private String logPath;

    /**
     * 执行器日志保存天数
     */
    @Value("${xxl.job.executor.logretentiondays}")
    private int logRetentionDays;

    @Bean(initMethod = "start", destroyMethod = "destroy")
    public XxlJobSpringExecutor xxlJobExecutor() {
        log.info("xxl-job-executor config init.");
        XxlJobSpringExecutor xxlJobSpringExecutor = new XxlJobSpringExecutor();
        xxlJobSpringExecutor.setAdminAddresses(adminAddresses);
        xxlJobSpringExecutor.setAppName(appName);
        xxlJobSpringExecutor.setIp(ip);
        xxlJobSpringExecutor.setPort(port);
        xxlJobSpringExecutor.setAccessToken(accessToken);
        xxlJobSpringExecutor.setLogPath(logPath);
        xxlJobSpringExecutor.setLogRetentionDays(logRetentionDays);

        return xxlJobSpringExecutor;
    }
}
