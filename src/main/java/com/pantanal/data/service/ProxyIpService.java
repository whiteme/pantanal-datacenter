/**
 * 
 */
package com.pantanal.data.service;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

  /**
   * 
   * @param ip
   * @return
   */
  public void checkProxyIp() {
    long size = redisService.getRedisTemplate().opsForList().size(ip_pending_list_key);
    String ipPort;
    long start = System.currentTimeMillis();
    String[] ips;
    for (int i = 0; i < size; i++) {
      start = System.currentTimeMillis();
      ipPort = (String) redisService.getRedisTemplate().opsForList().index(ip_pending_list_key, i);
      ipPort = StringUtils.remove(ipPort, "HTTP://");
      ips = StringUtils.split(ipPort, ":");
      try (CloseableHttpClient closeableHttpClient = HttpClientBuilder.create().build()) {
        HttpHost proxy = new HttpHost(ips[0], NumberUtils.toInt(ips[1]));
        RequestConfig config = RequestConfig.custom().setProxy(proxy).build();
        HttpPost httpPost = new HttpPost("http://httpbin.org/ip");
        httpPost.setConfig(config);
        try (CloseableHttpResponse response = closeableHttpClient.execute(httpPost)) {
          long time = System.currentTimeMillis() - start;
          redisService.zAdd("PROXY_IP_POOL", ips[0] + ":" + ips[1], time);
          log.info("======valid proxy===" + ips[0] + ":" + ips[1] + " timeout:" + time);
        } catch (Exception e) {
          log.error("===CloseableHttpClient ip:" + ips[0] + "[" + ips[1] + "] response error!");
        }
      } catch (Exception e) {
        log.error("===CloseableHttpClient ip:" + ips[0] + "[" + ips[1] + "]  error!");
      }
    }
  }

}
