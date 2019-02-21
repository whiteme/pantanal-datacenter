package com.pantanal.data.task;

import com.pantanal.data.cache.redis.RedisClient;
import org.apache.http.HttpHost;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.util.Map;
import java.util.Properties;

public class ProxyValidateTask extends BaseTask {

    private static Logger logger = LoggerFactory.getLogger(ProxyValidateTask.class);

    private String checkUrl ;

    private RedisClient rc;

    private String proxyHost;

    private String proxyPort;

    private CloseableHttpClient hc ;

    public ProxyValidateTask(CloseableHttpClient hc , Map param , Properties conf) {
        super(param , conf);
        this.hc = hc;
        this.rc = new RedisClient("127.0.0.1" , "DaSiNi");
        this.proxyHost = param.get("proxyHost").toString();
        this.proxyPort = param.get("proxyPort").toString();
        this.checkUrl = param.get("checkUrl").toString();

    }


    @Override
    public void run() {
        CloseableHttpResponse response = null;

        String proxyDesc = "HTTP://" + this.proxyHost + ":" + this.proxyPort;

        try {
            URIBuilder builder = new URIBuilder(this.checkUrl);
            URI uri = builder.build();
            HttpGet httpGet = new HttpGet(uri);
            HttpHost proxy = new HttpHost(this.proxyHost,Integer.parseInt(this.proxyPort));
            RequestConfig requestConfig = RequestConfig.custom()
                    .setProxy(proxy)
                    .setConnectTimeout(3000)
                    .setSocketTimeout(3000)
                    .setConnectionRequestTimeout(3000)
                    .build();
            httpGet.setConfig(requestConfig);

            long start = System.currentTimeMillis();
            response = hc.execute(httpGet);
            long cost = System.currentTimeMillis() - start;

            logger.info("Proxy Ip is : {}" , proxyDesc);
            if ( (response.getStatusLine().getStatusCode() + "").startsWith("2")  ) {
                rc.exec(jedis -> {
                      return   jedis.zadd("IP_DAILI_POOL" , cost , proxyDesc);
                });
                logger.info("==== SPOTTED Active PROXY {} ==== "  , proxyDesc);
            } else {
                logger.info("INVALID PROXY {}"  , proxyDesc);
            }
        } catch (Exception e) {
            logger.info("{} INVALID PROXY {}"  , proxyDesc , e.getMessage());
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
