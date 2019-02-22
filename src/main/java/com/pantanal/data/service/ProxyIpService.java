/**
 *
 */
package com.pantanal.data.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import com.pantanal.data.task.ProxyValidateTask;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.stereotype.Service;

/**
 * @author gudong
 * @version $Date:2019-02-09
 */
@Service
public class ProxyIpService {
    private static Logger log = LoggerFactory.getLogger(ProxyIpService.class);
    @Resource
    private RedisService redisService;
    private static String ip_pending_list_key = "IP_DAILI_POOL";

    private static ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(4, 16, 60L, TimeUnit.SECONDS, new SynchronousQueue<Runnable>());

    /**
     *
     * @param
     * @return
     */
    public void checkProxyIp() {

        Properties prop = new Properties();
        ListOperations<String, String> pendingPortIps = redisService.getRedisTemplate().opsForList();
        String ipPort;
        String[] ips;
        CloseableHttpClient hc = HttpClients.createDefault();
        int count = 0;
        while ((ipPort = pendingPortIps.leftPop(ip_pending_list_key)) != null) {
            ips = StringUtils.split(StringUtils.remove(ipPort, "HTTP://"), ":");
            try {
//                executeCheck(ips[0], ips.length <= 1 ? 80 : NumberUtils.toInt(ips[1]));

                doCheckTask(hc , ips[0] , ips.length <= 1 ? 80 + "" : NumberUtils.toInt(ips[1]) + "" , prop);
                count ++;
                log.info("Total check count {}" , count);
            } catch (Exception e) {
                log.debug("===executeCheck error! IPORT:" + ipPort, e);
            }
        }
        try {
            hc.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param ip
     * @param port
     */
    private void executeCheck(String ip, int port) {
        threadPoolExecutor.execute(new Runnable() {
            @Override
            public void run() {
                long start = System.currentTimeMillis();
                try (CloseableHttpClient closeableHttpClient = HttpClientBuilder.create().build()) {
                    HttpHost proxy = new HttpHost(ip, port);
                    RequestConfig config = RequestConfig.custom().setProxy(proxy).build();
                    HttpPost httpPost = new HttpPost("https://www.baidu.com");
                    httpPost.setConfig(config);
                    try (CloseableHttpResponse response = closeableHttpClient.execute(httpPost)) {
                        long time = System.currentTimeMillis() - start;
                        redisService.getRedisTemplate().opsForZSet().add("PROXY_IP_POOL", ip + ":" + port, time);
                        log.info("======valid proxy===" + ip + ":" + port + " timeout:" + time);
                    } catch (Exception e) {
                        log.debug("===CloseableHttpClient ip:" + ip + "[" + port + "] response error!", e);
                    }
                } catch (Exception e) {
                    log.debug("===CloseableHttpClient ip:" + ip + "[" + port + "]  error!", e);
                }
            }
        });
    }


    private void doCheckTask(CloseableHttpClient hc , String ip , String port , Properties properties){
         Map param = new HashMap();
         param.put("proxyHost" , ip);
         param.put("proxyPort" , port);
         param.put("checkUrl" , "https://www.baidu.com");

        threadPoolExecutor.execute(new ProxyValidateTask(hc , param , properties));
    }
}
