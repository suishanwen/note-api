package com.sw.note.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisNode;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;
import java.util.stream.Collectors;

import static io.lettuce.core.ReadFrom.REPLICA_PREFERRED;


@Configuration
public class RedisCentinelConfig {

    @Autowired
    RedisProperties redisProperties;

    @Bean
    public RedisConnectionFactory lettuceConnectionFactory() {
        RedisSentinelConfiguration sentinelConfig = new RedisSentinelConfiguration();
        RedisProperties.Sentinel sentinel = redisProperties.getSentinel();
        List<RedisNode> redisNodes = sentinel.getNodes().stream().map(url -> {
            String[] arr = url.split(":");
            return new RedisNode(arr[0], Integer.parseInt(arr[1]));
        }).collect(Collectors.toList());
        sentinelConfig.setMaster(sentinel.getMaster());
        sentinelConfig.setPassword(sentinel.getPassword());
        sentinelConfig.setSentinelPassword(sentinel.getPassword());
        sentinelConfig.setSentinels(redisNodes);
        LettuceClientConfiguration clientConfig = LettuceClientConfiguration.builder()
                .readFrom(REPLICA_PREFERRED)
                .build();
        return new LettuceConnectionFactory(sentinelConfig, clientConfig);
    }

    @Bean
    public RedisTemplate<String, Object> getRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        return template;
    }
}
