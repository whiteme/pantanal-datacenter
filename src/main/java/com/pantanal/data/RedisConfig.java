/**
 * 
 */
package com.pantanal.data;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @author gudong
 * @version $Date:2019-02-09
 */

@Configuration
public class RedisConfig {

  @Bean
  public StringRedisTemplate redisTemplate(RedisConnectionFactory redisConnectionFactory) {
    StringRedisTemplate template = new StringRedisTemplate();
    template.setConnectionFactory(redisConnectionFactory);

    RedisSerializer<String> stringSerializer = new StringRedisSerializer();
    template.setKeySerializer(stringSerializer );
    template.setValueSerializer(stringSerializer );
    template.setHashKeySerializer(stringSerializer );
    template.setHashValueSerializer(stringSerializer );

    return template;
  }
}
