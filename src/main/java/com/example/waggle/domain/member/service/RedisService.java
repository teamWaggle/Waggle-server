package com.example.waggle.domain.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.HashSet;
import java.util.Set;

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

    public void incrementCntInHash(String key, String hashKey, Long value) {
        HashOperations<String, String, Long> hashOperations = redisTemplate.opsForHash();
        hashOperations.increment(key, hashKey, value);
    }

    public Long getCntInHash(String key, String hashKey) {
        HashOperations<String, String, Long> hashOperations = redisTemplate.opsForHash();
        return hashOperations.get(key, hashKey);
    }

    public boolean existCntInHash(String key, String hashKey) {
        HashOperations<String, String, Long> hashOperations = redisTemplate.opsForHash();
        return hashOperations.hasKey(key, hashKey);
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

    public Set<String> getValuesInSet(String key) {
        SetOperations<String, String> setOperations = redisTemplate.opsForSet();
        return setOperations.members(key);
    }

    public Set<String> getKeysByPattern(String pattern) {
        RedisConnection connection = redisTemplate.getConnectionFactory().getConnection();
        ScanOptions scanOptions = ScanOptions.scanOptions().match(pattern).build();
        Cursor<byte[]> cursor = connection.scan(scanOptions);

        Set<String> keys = new HashSet<>();
        while (cursor.hasNext()) {
            keys.add(new String(cursor.next()));
        }

        try {
            cursor.close();
        } catch (Exception e) {
            // Handle exception
        }

        return keys;
    }

    public boolean existValueInSet(String key, String value) {
        SetOperations<String, String> setOperations = redisTemplate.opsForSet();
        return setOperations.isMember(key, value);
    }

    public void deleteValueInSet(String key, String value) {
        SetOperations<String, String> setOperations = redisTemplate.opsForSet();
        setOperations.remove(key, value);
    }

}