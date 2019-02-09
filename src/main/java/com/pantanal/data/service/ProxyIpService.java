/**
 * 
 */
package com.pantanal.data.service;

import javax.annotation.Resource;

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

  /**
   * 
   * @param ip
   * @return
   */
  public void checkProxyIp(String proxyIp, int proxyPort) {
    long start = System.currentTimeMillis();
    try (CloseableHttpClient closeableHttpClient = HttpClientBuilder.create().build()) {
      HttpHost proxy = new HttpHost(proxyIp, proxyPort);
      RequestConfig config = RequestConfig.custom().setProxy(proxy).build();
      HttpPost httpPost = new HttpPost("http://httpbin.org/ip");
      httpPost.setConfig(config);
      try (CloseableHttpResponse response = closeableHttpClient.execute(httpPost)) {
        long time = System.currentTimeMillis() - start;
        redisService.zAdd("proxypool", proxyIp + ":" + proxyPort, time);
      } catch (Exception e) {
        log.error("===CloseableHttpClient ip:" + proxyIp + "[" + proxyPort + "] response error!");
      }
    } catch (Exception e) {
      log.error("===CloseableHttpClient ip:" + proxyIp + "[" + proxyPort + "]  error!");
    }
  }
}
