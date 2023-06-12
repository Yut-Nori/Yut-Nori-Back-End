package com.example.yutnoribackend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisService {
    private final RedisTemplate<String, String> redisTemplate;

    // redis data 입력
    public void setValues(String key, String data){
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        values.set(key, data);
    }

    // redis data 입력시 만료 기한을 설정, {key, value, 제한시간, 시간단위}
    public void setValues(String key, String data, Long time, TimeUnit timeUnit){
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        values.set(key, data, time, timeUnit);
    }

    // redis data 얻기
    public String getValues(String key) {
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        return values.get(key);
    }

    // redis data 제거
    public void deleteValues(String key) {
        redisTemplate.delete(key);
    }
}
