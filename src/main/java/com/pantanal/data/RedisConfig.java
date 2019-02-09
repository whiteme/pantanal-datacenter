/**
 * 
 */
package com.pantanal.data;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @author gudong
 * @version $Date:2019-02-09
 */

@Configuration
public class RedisConfig {

  @Bean
  public RedisTemplate redisTemplate(RedisConnectionFactory redisConnectionFactory) {
    RedisTemplate template = new RedisTemplate();
    template.setConnectionFactory(redisConnectionFactory);
    return template;
  }
}
