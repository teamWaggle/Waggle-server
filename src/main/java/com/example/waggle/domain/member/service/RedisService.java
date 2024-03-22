package com.example.waggle.domain.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RedisService {

    private final RedisTemplate redisTemplate;

    // key-value 설정
    public void setValue(String token, String username) {
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        values.set(token, username, Duration.ofDays(7));
    }

    // key 값으로 value 가져오기
    public String getValue(String token) {
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        return values.get(token);
    }

    public void deleteValue(String token) {
        if (token != null && token.startsWith("Bearer ")) {
            // "Bearer " 접두사 제거
            token = token.substring(7);
        }
        redisTemplate.delete(token);
    }

    public void setValueWithExpiration(String token, String value, long expireInMillis) {
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        values.set(token, value, Duration.ofMillis(expireInMillis));
    }

    //for recommend count & view count
    public void setCntInHash(String key, String hashKey, Long value) {
        HashOperations<String, String, Long> hashOperations = redisTemplate.opsForHash();
        hashOperations.put(key, hashKey, value);
    }

    public Long getCntInHash(String key, String hashKey) {
        HashOperations<String, String, Long> hashOperations = redisTemplate.opsForHash();
        return hashOperations.get(key, hashKey);
    }

    public void deleteCntInHash(String key) {
        HashOperations<String, String, Long> hashOperations = redisTemplate.opsForHash();
        hashOperations.delete(key);
    }

    //for newly insert recommend
    public void setValueInSet(String key, String value) {
        SetOperations<String, String> setOperations = redisTemplate.opsForSet();
        setOperations.add(key, value);
    }

    public boolean existValueInSet(String key, String value) {
        SetOperations<String, String> setOperations = redisTemplate.opsForSet();
        return setOperations.isMember(key, value);
    }

    public void deleteValueInSet(String key, List<String> values) {
        SetOperations<String, String> setOperations = redisTemplate.opsForSet();
        setOperations.remove(key, values);
    }

}